package com.inventrack.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;
    private BigDecimal discount;

    public Cart() {
        this.items = new ArrayList<>();
        this.discount = BigDecimal.ZERO;
    }

    public List<CartItem> getItems() { return items; }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProduct().getId() == productId);
    }
    
    public void clear() {
        items.clear();
        discount = BigDecimal.ZERO;
    }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getSubtotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
    
    public BigDecimal getTotal() {
        BigDecimal total = getSubtotal().subtract(discount);
        return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
    }
}
