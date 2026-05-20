package com.inventrack.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory shopping cart for the POS workflow.
 *
 * <p>Stored in HTTP session under {@code "cart"} and converted into an {@code order} during checkout.</p>
 */
public class Cart {
    private List<CartItem> items;
    private BigDecimal discount;

    public Cart() {
        this.items = new ArrayList<>();
        this.discount = BigDecimal.ZERO;
    }

    public List<CartItem> getItems() { return items; }

    public void addItem(Product product, int quantity) {
        // Step 1: If the product already exists in the cart, increase its quantity.
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Step 2: Otherwise append a new cart line.
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(int productId) {
        // Remove by product id (product is the line item identity for this cart).
        items.removeIf(item -> item.getProduct().getId() == productId);
    }
    
    public void clear() {
        // Reset cart to an empty state for a new transaction.
        items.clear();
        discount = BigDecimal.ZERO;
    }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getSubtotal() {
        // Step 1: Sum each line item subtotal.
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        // Step 2: Return subtotal (before discount/taxes).
        return total;
    }
    
    public BigDecimal getTotal() {
        // Step 1: Apply discount.
        BigDecimal total = getSubtotal().subtract(discount);
        // Step 2: Clamp at 0 so discounts can't make totals negative.
        return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
    }
}
