package com.inventrack.dao;

import com.inventrack.config.DBConnection;
import com.inventrack.model.CartItem;
import com.inventrack.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class OrderDAO {

    public boolean createOrder(Order order, List<CartItem> cartItems) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, discount, status) VALUES (?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        String stockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Order
            int orderId = 0;
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getUserId());
                orderStmt.setBigDecimal(2, order.getTotalAmount());
                orderStmt.setBigDecimal(3, order.getDiscount());
                orderStmt.setString(4, order.getStatus());
                orderStmt.executeUpdate();

                try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new Exception("Failed to get order ID.");
                    }
                }
            }

            // 2. Insert Order Items & 3. Update Stock
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql);
                 PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                 
                for (CartItem item : cartItems) {
                    // Insert Item
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getProduct().getPrice());
                    itemStmt.setBigDecimal(5, item.getSubtotal());
                    itemStmt.addBatch();

                    // Deduct Stock
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProduct().getId());
                    stockStmt.addBatch();
                }
                
                itemStmt.executeBatch();
                stockStmt.executeBatch();
            }

            conn.commit(); // End transaction
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new java.util.ArrayList<>();
        String sql = "SELECT o.*, u.full_name as cashier_name " +
                     "FROM orders o " +
                     "LEFT JOIN users u ON o.user_id = u.id " +
                     "ORDER BY o.created_at DESC";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCashierName(rs.getString("cashier_name"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setDiscount(rs.getBigDecimal("discount"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public java.math.BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE status = 'COMPLETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                java.math.BigDecimal rev = rs.getBigDecimal(1);
                return rev != null ? rev : java.math.BigDecimal.ZERO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return java.math.BigDecimal.ZERO;
    }

    public int getTotalOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = 'COMPLETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
