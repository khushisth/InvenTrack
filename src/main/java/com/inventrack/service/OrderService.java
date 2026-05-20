package com.inventrack.service;

import com.inventrack.dao.OrderDAO;
import com.inventrack.model.Cart;
import com.inventrack.model.Order;
import com.inventrack.model.User;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public boolean processCheckout(Cart cart, User cashier) {
        if (cart == null || cart.getItems().isEmpty()) {
            return false;
        }

        Order order = new Order();
        order.setUserId(cashier.getId());
        order.setTotalAmount(cart.getTotal());
        order.setDiscount(cart.getDiscount());
        order.setStatus("COMPLETED");

        return orderDAO.createOrder(order, cart.getItems());
    }
}
