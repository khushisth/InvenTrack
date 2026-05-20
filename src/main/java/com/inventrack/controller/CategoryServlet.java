package com.inventrack.controller;

import com.inventrack.model.Category;
import com.inventrack.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Category maintenance controller (Stock Manager only).
 *
 * <p>URL mapping: {@code /categories}</p>
 *
 * <p>Authorization: requires session role {@code "Stock Manager"}.</p>
 */
@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Authorize.
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can access categories.");
            return;
        }
        // Step 2: Load categories and render JSP.
        request.setAttribute("categories", categoryService.getAllCategories());
        request.getRequestDispatcher("/categories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Authorize.
        if (!isStockManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can manage categories.");
            return;
        }

        // Step 2: Read and validate form input.
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String description = request.getParameter("description") != null ? request.getParameter("description").trim() : "";

        if (name.isEmpty()) {
            request.setAttribute("errorMessage", "Category name is required.");
            request.setAttribute("categories", categoryService.getAllCategories());
            request.getRequestDispatcher("/categories.jsp").forward(request, response);
            return;
        }

        // Step 3: Persist.
        Category category = new Category(0, name, description);
        boolean added = categoryService.addCategory(category);

        if (!added) {
            request.setAttribute("errorMessage", "Unable to add category. Please verify database setup and try again.");
            request.setAttribute("categories", categoryService.getAllCategories());
            request.getRequestDispatcher("/categories.jsp").forward(request, response);
            return;
        }

        // Step 4: PRG redirect back to list.
        response.sendRedirect(request.getContextPath() + "/categories");
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
