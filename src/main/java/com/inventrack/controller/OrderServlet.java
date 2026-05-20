package com.inventrack.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Backward-compatible route alias for order history.
 *
 * <p>URL mapping: {@code /orders}</p>
 *
 * <p>This servlet exists as a redirect to {@code /transactions} which is the actual controller that
 * renders {@code orders.jsp}.</p>
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Redirect to the canonical transactions route.
        response.sendRedirect(request.getContextPath() + "/transactions");
    }
}
