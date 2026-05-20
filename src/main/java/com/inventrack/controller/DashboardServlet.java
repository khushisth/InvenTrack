package com.inventrack.controller;

import com.inventrack.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Renders the admin/dashboard overview.
 *
 * <p>URL mapping: {@code /dashboard}</p>
 *
 * <p>Request attributes set:</p>
 * <ul>
 *   <li>{@code totalProducts}</li>
 *   <li>{@code lowStockCount}</li>
 *   <li>{@code totalRevenue}</li>
 *   <li>{@code totalOrders}</li>
 *   <li>{@code recentOrders}</li>
 * </ul>
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Load dashboard metrics.
        request.setAttribute("totalProducts", dashboardService.getTotalProducts());
        request.setAttribute("lowStockCount", dashboardService.getLowStockCount());
        request.setAttribute("totalRevenue", dashboardService.getTotalRevenue());
        request.setAttribute("totalOrders", dashboardService.getTotalOrders());
        request.setAttribute("recentOrders", dashboardService.getRecentOrders(5));

        // Step 2: Forward to JSP for rendering.
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
