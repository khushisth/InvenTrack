package com.inventrack.controller;

import com.inventrack.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalProducts", dashboardService.getTotalProducts());
        request.setAttribute("lowStockCount", dashboardService.getLowStockCount());
        request.setAttribute("totalRevenue", dashboardService.getTotalRevenue());
        request.setAttribute("totalOrders", dashboardService.getTotalOrders());
        request.setAttribute("recentOrders", dashboardService.getRecentOrders(5));

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
