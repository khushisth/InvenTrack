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

/**
 * Supplier maintenance controller (Stock Manager only).
 *
 * <p>URL mapping: {@code /suppliers}</p>
 *
 * <p>Authorization: requires session role {@code "Stock Manager"}.</p>
 */
@WebServlet("/suppliers")
public class SupplierServlet extends HttpServlet {
    private SupplierService supplierService;

    @Override
    public void init() throws ServletException {
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Authorize.
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can access suppliers.");
            return;
        }
        // Step 2: Load suppliers and render JSP.
        request.setAttribute("suppliers", supplierService.getAllSuppliers());
        request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Authorize.
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can manage suppliers.");
            return;
        }

        // Step 2: Read and validate form input.
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String contactInfo = request.getParameter("contactInfo") != null ? request.getParameter("contactInfo").trim() : "";
        String productsSupplied = request.getParameter("productsSupplied") != null ? request.getParameter("productsSupplied").trim() : "";

        if (name.isEmpty()) {
            request.setAttribute("errorMessage", "Supplier name is required.");
            request.setAttribute("suppliers", supplierService.getAllSuppliers());
            request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
            return;
        }

        // Step 3: Persist.
        Supplier supplier = new Supplier(0, name, contactInfo, productsSupplied);
        boolean added = supplierService.addSupplier(supplier);

        if (!added) {
            request.setAttribute("errorMessage", "Unable to add supplier. Please verify database setup and try again.");
            request.setAttribute("suppliers", supplierService.getAllSuppliers());
            request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
            return;
        }

        // Step 4: PRG redirect back to list.
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }

    private boolean isStockManager(HttpServletRequest request) {
        // Reads the role from session (set during login).
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object role = session.getAttribute("role");
        return role != null && "Stock Manager".equalsIgnoreCase(role.toString());
    }
}
