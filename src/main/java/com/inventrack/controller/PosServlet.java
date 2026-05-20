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

@WebServlet("/pos")
public class PosServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Ensure cart exists
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if ("finish".equals(action)) {
            cart.clear();
            response.sendRedirect(request.getContextPath() + "/pos");
            return;
        }

        String query = request.getParameter("query");
        if (query != null && !query.trim().isEmpty()) {
            request.setAttribute("products", productService.searchProducts(query));
        } else {
            // Load all or limit for POS terminal
            request.setAttribute("products", productService.getAllProducts());
        }

        request.getRequestDispatcher("/pos.jsp").forward(request, response);
    }
}
