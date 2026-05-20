package com.inventrack.service;

import com.inventrack.dao.OrderDAO;
import com.inventrack.model.Order;

import java.util.List;

/**
 * Transaction/history read operations.
 *
 * <p>This service currently reads from the {@code orders} table (completed checkout records).</p>
 */
public class TransactionService {
    private final OrderDAO orderDAO;

    public TransactionService() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * @return all orders, newest first
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
