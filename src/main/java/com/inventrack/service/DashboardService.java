package com.inventrack.service;

import com.inventrack.dao.OrderDAO;
import com.inventrack.dao.ProductDAO;
import com.inventrack.model.Order;
import com.inventrack.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class DashboardService {
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public DashboardService() {
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
    }

    public int getTotalProducts() {
        return productDAO.getAllProducts().size();
    }

    public long getLowStockCount() {
        List<Product> products = productDAO.getAllProducts();
        return products.stream().filter(Product::isLowStock).count();
    }

    public BigDecimal getTotalRevenue() {
        return orderDAO.getTotalRevenue();
    }

    public int getTotalOrders() {
        return orderDAO.getTotalOrderCount();
    }

    public List<Order> getRecentOrders(int limit) {
        List<Order> orders = orderDAO.getAllOrders();
        if (orders.size() <= limit) {
            return orders;
        }
        return orders.subList(0, limit);
    }
}
