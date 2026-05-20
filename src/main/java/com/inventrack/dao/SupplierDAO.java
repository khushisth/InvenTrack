package com.inventrack.dao;

import com.inventrack.config.DBConnection;
import com.inventrack.model.Supplier;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for {@code suppliers}.
 *
 * <p>This DAO is written defensively to tolerate older/newer schema variants by checking whether
 * optional columns exist before reading/writing them.</p>
 */
public class SupplierDAO {

    /**
     * @return all suppliers (empty list on error)
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Optional columns: only read them if present in the current schema/result set.
                String contactInfo = hasResultColumn(rs, "contact_info") ? rs.getString("contact_info") : "";
                String productsSupplied = hasResultColumn(rs, "products_supplied") ? rs.getString("products_supplied") : "";
                suppliers.add(new Supplier(
                    rs.getInt("id"),
                    rs.getString("name"),
                    contactInfo,
                    productsSupplied
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Inserts a supplier, writing optional columns only if they exist in the DB schema.
     *
     * @param supplier supplier to insert
     * @return true if inserted successfully
     */
    public boolean addSupplier(Supplier supplier) {
        try (Connection conn = DBConnection.getConnection()) {
            // Step 1: Detect optional columns so the INSERT is compatible with the actual DB schema.
            boolean hasContactInfo = hasColumn(conn, "suppliers", "contact_info");
            boolean hasProductsSupplied = hasColumn(conn, "suppliers", "products_supplied");

            // Step 2: Build a dynamic INSERT statement based on the detected schema.
            StringBuilder columnSql = new StringBuilder("name");
            StringBuilder valueSql = new StringBuilder("?");
            List<String> values = new ArrayList<>();
            values.add(supplier.getName());

            if (hasContactInfo) {
                columnSql.append(", contact_info");
                valueSql.append(", ?");
                values.add(supplier.getContactInfo());
            }

            if (hasProductsSupplied) {
                columnSql.append(", products_supplied");
                valueSql.append(", ?");
                values.add(supplier.getProductsSupplied());
            }

            String sql = "INSERT INTO suppliers (" + columnSql + ") VALUES (" + valueSql + ")";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Step 3: Bind values and normalize empty strings to NULL.
                for (int i = 0; i < values.size(); i++) {
                    String value = values.get(i);
                    if (value == null || value.trim().isEmpty()) {
                        stmt.setNull(i + 1, Types.VARCHAR);
                    } else {
                        stmt.setString(i + 1, value.trim());
                    }
                }
                // Step 4: Execute insert.
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks the DB schema for a column's existence.
     */
    private boolean hasColumn(Connection conn, String tableName, String columnName) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getColumns(conn.getCatalog(), null, tableName, columnName)) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Checks whether a column exists in the current {@link ResultSet}.
     */
    private boolean hasResultColumn(ResultSet rs, String columnName) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}
