package com.inventrack.controller;

import java.io.IOException;

import com.inventrack.model.User;
import com.inventrack.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles self-service registration.
 *
 * <p>URL mapping: {@code /register}</p>
 *
 * <p>Registration creates a user record with a BCrypt-hashed password. Non-admin users must be approved
 * by an admin before they are allowed to log in.</p>
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Render the registration form.
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Read and validate form input.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String roleIdRaw = request.getParameter("roleId");

        if (isBlank(username) || isBlank(password) || isBlank(confirmPassword) || isBlank(fullName) || isBlank(roleIdRaw)) {
            request.setAttribute("errorMessage", "Please fill out all required fields.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Password and confirm password do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Step 2: Parse role selection.
        int roleId;
        try {
            roleId = Integer.parseInt(roleIdRaw);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid role selected.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Step 3: Enforce allowed roles for self-registration.
        if (roleId < 1 || roleId > 3) {
            request.setAttribute("errorMessage", "Invalid role selected.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setFullName(fullName.trim());
        user.setEmail(email != null ? email.trim() : null);
        user.setPhone(phone != null ? phone.trim() : null);
        user.setRoleId(roleId);

        try {
            // Step 5: Persist registration (password hashing + flags happen in AuthService/DAO).
            boolean registered = authService.register(user);
            if (registered) {
                // Step 6: Show a one-time message on the login page.
                request.getSession().setAttribute(
                    "successMessage",
                    "Registration submitted. Your account needs admin approval before login."
                );
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            request.setAttribute("errorMessage", "Registration failed. Username or email may already exist.");
        } catch (RuntimeException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        // Step 7: Re-render form with error.
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
