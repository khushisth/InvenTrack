package com.inventrack.service;

import com.inventrack.dao.OrderDAO;
import com.inventrack.model.Cart;
import com.inventrack.model.Order;
import com.inventrack.model.User;

/**
 * Checkout/order business logic.
 *
 * <p>Converts the in-memory {@link Cart} into a persisted {@link Order} and related {@code order_items},
 * and updates product stock levels inside a single DB transaction.</p>
 */
public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Persists an order for the given cart and cashier.
     *
     * @param cart current cart from session
     * @param cashier logged-in user performing the transaction
     * @return true if the checkout transaction completed successfully
     */
    public boolean processCheckout(Cart cart, User cashier) {
        // Step 1: Guard against empty carts.
        if (cart == null || cart.getItems().isEmpty()) {
            return false;
        }

        // Step 2: Build the order header from the cart totals and cashier identity.
        Order order = new Order();
        order.setUserId(cashier.getId());
        order.setTotalAmount(cart.getTotal());
        order.setDiscount(cart.getDiscount());
        order.setStatus("COMPLETED");

        // Step 3: Delegate atomic persistence to the DAO.
        return orderDAO.createOrder(order, cart.getItems());
    }
}
