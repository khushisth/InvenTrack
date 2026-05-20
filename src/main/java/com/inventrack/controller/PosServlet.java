package com.inventrack.controller;

import com.inventrack.model.Cart;
import com.inventrack.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Point-of-sale terminal screen controller.
 *
 * <p>URL mapping: {@code /pos}</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Ensure a {@link Cart} exists in the session.</li>
 *   <li>Search products by query or load all products.</li>
 *   <li>Handle a lightweight "finish" action that clears the cart after printing receipt.</li>
 * </ul>
 */
@WebServlet("/pos")
public class PosServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Load/create the cart stored in the session.
        HttpSession session = request.getSession();
        
        // Ensure cart exists.
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Step 2: Optional finish action: clear the cart and refresh the POS.
        String action = request.getParameter("action");
        if ("finish".equals(action)) {
            cart.clear();
            response.sendRedirect(request.getContextPath() + "/pos");
            return;
        }

        // Step 3: Search products if query provided; otherwise load all.
        String query = request.getParameter("query");
        if (query != null && !query.trim().isEmpty()) {
            request.setAttribute("products", productService.searchProducts(query));
        } else {
            // Load all or limit for POS terminal
            request.setAttribute("products", productService.getAllProducts());
        }

        // Step 4: Render JSP.
        request.getRequestDispatcher("/pos.jsp").forward(request, response);
    }
}
