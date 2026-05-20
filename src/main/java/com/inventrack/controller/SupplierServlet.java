package com.inventrack.controller;

import com.inventrack.model.Supplier;
import com.inventrack.service.SupplierService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/suppliers")
public class SupplierServlet extends HttpServlet {
    private SupplierService supplierService;

    @Override
    public void init() throws ServletException {
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can access suppliers.");
            return;
        }
        request.setAttribute("suppliers", supplierService.getAllSuppliers());
        request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can manage suppliers.");
            return;
        }

        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String contactInfo = request.getParameter("contactInfo") != null ? request.getParameter("contactInfo").trim() : "";
        String productsSupplied = request.getParameter("productsSupplied") != null ? request.getParameter("productsSupplied").trim() : "";

        if (name.isEmpty()) {
            request.setAttribute("errorMessage", "Supplier name is required.");
            request.setAttribute("suppliers", supplierService.getAllSuppliers());
            request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
            return;
        }

        Supplier supplier = new Supplier(0, name, contactInfo, productsSupplied);
        boolean added = supplierService.addSupplier(supplier);

        if (!added) {
            request.setAttribute("errorMessage", "Unable to add supplier. Please verify database setup and try again.");
            request.setAttribute("suppliers", supplierService.getAllSuppliers());
            request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/suppliers");
    }

    private boolean isStockManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("role");
        return role != null && "Stock Manager".equalsIgnoreCase(role.toString());
    }
}
