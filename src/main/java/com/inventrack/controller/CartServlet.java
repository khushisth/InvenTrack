package com.inventrack.controller;

import com.inventrack.model.Cart;
import com.inventrack.model.Product;
import com.inventrack.model.User;
import com.inventrack.service.OrderService;
import com.inventrack.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Mutates the session cart and performs checkout.
 *
 * <p>URL mapping: {@code /cart}</p>
 *
 * <p>Supported POST actions:</p>
 * <ul>
 *   <li>{@code add} - add quantity of a product to the cart</li>
 *   <li>{@code remove} - remove a product from the cart</li>
 *   <li>{@code clear} - clear cart</li>
 *   <li>{@code checkout} - create an order and decrement stock</li>
 * </ul>
 *
 * <p>Session attributes used:</p>
 * <ul>
 *   <li>{@code cart} - {@link Cart}</li>
 *   <li>{@code user} - {@link User} (cashier)</li>
 *   <li>{@code checkoutSuccess}/{@code checkoutError} - flash messages for the POS/receipt pages</li>
 * </ul>
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Read action and get/create the session cart.
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        if ("add".equals(action)) {
            // Step 2a: Add a product to the cart.
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            // For simplicity, this uses the full product list to locate a product.
            // A more efficient approach is a DAO method like getProductById(productId).
            Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId() == productId)
                .findFirst().orElse(null);
                
            if (product != null) {
                cart.addItem(product, quantity);
            }
        } else if ("remove".equals(action)) {
            // Step 2b: Remove a product from the cart.
            int productId = Integer.parseInt(request.getParameter("productId"));
            cart.removeItem(productId);
        } else if ("clear".equals(action)) {
            // Step 2c: Clear the cart.
            cart.clear();
        } else if ("checkout".equals(action)) {
            // Step 2d: Persist order and update stock.
            User user = (User) session.getAttribute("user");
            if (orderService.processCheckout(cart, user)) {
                // Do not clear cart yet so the receipt page can render the purchased items.
                request.getSession().setAttribute("checkoutSuccess", "Transaction completed successfully!");
                response.sendRedirect(request.getContextPath() + "/receipt.jsp");
                return;
            } else {
                request.getSession().setAttribute("checkoutError", "Transaction failed.");
            }
        }

        // Step 3: Return to POS screen.
        response.sendRedirect(request.getContextPath() + "/pos");
    }
}
