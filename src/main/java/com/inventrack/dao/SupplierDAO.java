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

public class SupplierDAO {

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
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

    public boolean addSupplier(Supplier supplier) {
        try (Connection conn = DBConnection.getConnection()) {
            boolean hasContactInfo = hasColumn(conn, "suppliers", "contact_info");
            boolean hasProductsSupplied = hasColumn(conn, "suppliers", "products_supplied");

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
                for (int i = 0; i < values.size(); i++) {
                    String value = values.get(i);
                    if (value == null || value.trim().isEmpty()) {
                        stmt.setNull(i + 1, Types.VARCHAR);
                    } else {
                        stmt.setString(i + 1, value.trim());
                    }
                }
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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
