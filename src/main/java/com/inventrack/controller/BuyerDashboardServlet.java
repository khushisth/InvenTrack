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

@WebServlet("/buyer-dashboard")
public class BuyerDashboardServlet extends HttpServlet {
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

        String query = request.getParameter("query");
        if (query != null && !query.trim().isEmpty()) {
            request.setAttribute("products", productService.searchProducts(query));
        } else {
            request.setAttribute("products", productService.getAllProducts());
        }

        request.getRequestDispatcher("/buyer-dashboard.jsp").forward(request, response);
    }
}
