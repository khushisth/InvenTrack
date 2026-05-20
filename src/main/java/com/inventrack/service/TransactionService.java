package com.inventrack.service;

import com.inventrack.dao.OrderDAO;
import com.inventrack.model.Order;

import java.util.List;

public class TransactionService {
    private final OrderDAO orderDAO;

    public TransactionService() {
        this.orderDAO = new OrderDAO();
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
