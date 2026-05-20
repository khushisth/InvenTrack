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
            request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String username = request.getParameter("username");
            String password =  request.getParameter("password");

        try {
            User user = authService.login(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRoleName());
                
                // Redirect based on role
                if ("Admin".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else if ("Cashier".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/pos");
                } else if ("Stock Manager".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/products");
                } else if ("Buyer".equalsIgnoreCase(user.getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/buyer-dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
