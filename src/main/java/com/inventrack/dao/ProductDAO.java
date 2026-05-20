package com.inventrack.dao;

import com.inventrack.config.DBConnection;
import com.inventrack.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for {@code products} and related lookup values.
 *
 * <p>Most queries join {@code categories} and {@code suppliers} to populate the display names used by
 * the inventory/POS UI.</p>
 */
public class ProductDAO {

    /**
     * Reads all products.
     *
     * @return all products (empty list on error)
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id";
                     
        // Step 1: Execute the query.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Step 2: Map rows into Product objects.
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setSku(rs.getString("sku"));
                p.setBarcode(rs.getString("barcode"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setSupplierName(rs.getString("supplier_name"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setCostPrice(rs.getBigDecimal("cost_price"));
                p.setStockQuantity(rs.getInt("stock_quantity"));
                p.setReorderLevel(rs.getInt("reorder_level"));
                p.setShelfLocation(rs.getString("shelf_location"));
                p.setUnitOfMeasure(rs.getString("unit_of_measure"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Searches products by name, SKU, or barcode.
     *
     * @param query search string (not null-safe; callers should guard)
     * @return matching products (empty list on error)
     */
    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                     "WHERE p.name LIKE ? OR p.sku LIKE ? OR p.barcode LIKE ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Step 1: Bind the LIKE parameters.
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            // Step 2: Execute and map results.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setSku(rs.getString("sku"));
                    p.setBarcode(rs.getString("barcode"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setCategoryName(rs.getString("category_name"));
                    p.setSupplierId(rs.getInt("supplier_id"));
                    p.setSupplierName(rs.getString("supplier_name"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setCostPrice(rs.getBigDecimal("cost_price"));
                    p.setStockQuantity(rs.getInt("stock_quantity"));
                    p.setReorderLevel(rs.getInt("reorder_level"));
                    p.setShelfLocation(rs.getString("shelf_location"));
                    p.setUnitOfMeasure(rs.getString("unit_of_measure"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    p.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Inserts a new product record.
     *
     * @param p product data to insert
     * @return true if inserted successfully
     */
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (name, sku, barcode, category_id, supplier_id, price, cost_price, stock_quantity, reorder_level, shelf_location, unit_of_measure) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Step 1: Bind statement parameters in column order.
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getSku());
            stmt.setString(3, p.getBarcode());
            stmt.setInt(4, p.getCategoryId());
            stmt.setInt(5, p.getSupplierId());
            stmt.setBigDecimal(6, p.getPrice());
            stmt.setBigDecimal(7, p.getCostPrice());
            stmt.setInt(8, p.getStockQuantity());
            stmt.setInt(9, p.getReorderLevel());
            stmt.setString(10, p.getShelfLocation());
            stmt.setString(11, p.getUnitOfMeasure());
            
            // Step 2: Execute insert.
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
