package model.dao;

import model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = inventrack.utils.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact_info"),
                        rs.getString("products_supplied")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, contact_info, products_supplied) VALUES (?, ?, ?)";
        try (Connection conn = inventrack.utils.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactInfo());
            stmt.setString(3, supplier.getProductsSupplied());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
SupplierDAO