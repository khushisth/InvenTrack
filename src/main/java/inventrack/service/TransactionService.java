package service;

import java.util.List;

public class TransactionService {
    private final OrdeeDAO orderDAO;

    public TransactionService() {
        this.orderDAO = new OrderDAO();
    }

    public List<inventrack.model.Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}