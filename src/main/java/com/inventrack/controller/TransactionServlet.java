package com.inventrack.controller;

import com.inventrack.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Lists order/transaction history.
 *
 * <p>URL mapping: {@code /transactions}</p>
 *
 * <p>Request attributes set:</p>
 * <ul>
 *   <li>{@code orders} - list of {@link com.inventrack.model.Order}</li>
 * </ul>
 */
@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private TransactionService transactionService;

    @Override
    public void init() throws ServletException {
        transactionService = new TransactionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Load orders.
        request.setAttribute("orders", transactionService.getAllOrders());
        // Step 2: Render JSP.
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
