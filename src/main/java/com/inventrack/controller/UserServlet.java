package com.inventrack.controller;

import java.io.IOException;
import java.util.List;

import com.inventrack.model.User;
import com.inventrack.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Admin-only user management screen.
 *
 * <p>URL mapping: {@code /users}</p>
 *
 * <p>Authorization: only users with role {@code "Admin"} may access/perform actions.</p>
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Require authentication.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Step 2: Require admin role.
        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equalsIgnoreCase(currentUser.getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        // Step 3: Load users and render view.
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Require authentication.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Step 2: Require admin role.
        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equalsIgnoreCase(currentUser.getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        // Step 3: Handle admin actions (currently: approve).
        String action = request.getParameter("action");
        if ("approve".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean success = userService.approveUser(userId);
            if (success) {
                request.setAttribute("successMessage", "User approved successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to approve user.");
            }
        }

        // Step 4: Reload the user list and render view (simple server-side "refresh").
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
