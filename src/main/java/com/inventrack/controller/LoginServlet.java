package com.inventrack.controller;

import com.inventrack.model.User;
import com.inventrack.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles login for all user roles.
 *
 * <p>URL mapping: {@code /login}</p>
 *
 * <p>On successful login, this servlet stores these session attributes:</p>
 * <ul>
 *   <li>{@code user} - {@link User}</li>
 *   <li>{@code role} - role name string (Admin, Cashier, Stock Manager)</li>
 * </ul>
 *
 * <p>Then it redirects users to a role-appropriate landing screen.</p>
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            // Step 1: Render the login page.
            request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            // Step 1: Read form parameters.
            String username = request.getParameter("username");
            String password =  request.getParameter("password");

        try {
            // Step 2: Authenticate credentials via the AuthService.
            User user = authService.login(username, password);
            if (user != null) {
                // Step 3: Create/attach session state.
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRoleName());
                
                // Step 4: Redirect based on role.
                if ("Admin".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else if ("Cashier".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/pos");
                } else if ("Stock Manager".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/products");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } else {
                // Step 5: Authentication failure; re-render form with an error message.
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            // Step 6: Known authentication errors (e.g., pending approval).
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
