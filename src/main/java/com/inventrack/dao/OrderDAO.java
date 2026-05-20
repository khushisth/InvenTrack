package com.inventrack.dao;

import com.inventrack.config.DBConnection;
import com.inventrack.model.CartItem;
import com.inventrack.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Data access for checkout/orders.
 *
 */
public class OrderDAO {

    /**
     * Creates an order and related line items, then decrements product stock.
     *
     * <p>All operations run inside a single DB transaction to keep order data and stock consistent.</p>
     *
     * @param order order header (userId, totals, status)
     * @param cartItems items from the in-memory cart
     * @return true if the transaction committed successfully
     */
    public boolean createOrder(Order order, List<CartItem> cartItems) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, discount, status) VALUES (?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        String stockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

        Connection conn = null;
        try {
            // Step 1: Open a connection and start a transaction.
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 2: Insert order header and capture generated order id.
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

            // Step 3: Insert order items and decrement stock for each product.
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql);
                 PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                 
                for (CartItem item : cartItems) {
                    // 3a) Insert a line item row.
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getProduct().getPrice());
                    itemStmt.setBigDecimal(5, item.getSubtotal());
                    itemStmt.addBatch();

                    // 3b) Decrement stock for the purchased quantity.
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProduct().getId());
                    stockStmt.addBatch();
                }
                
                itemStmt.executeBatch();
                stockStmt.executeBatch();
            }

            // Step 4: Commit transaction.
            conn.commit(); // End transaction
            return true;

        } catch (Exception e) {
            // Step 5: Rollback on any failure.
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Step 6: Restore autocommit and close connection.
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    /**
     * Reads all orders with cashier name.
     *
     * @return orders newest-first
     */
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

    /**
     * @return sum of total_amount for completed orders
     */
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

    /**
     * @return number of completed orders
     */
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
