package com.inventrack.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Global authentication/authorization filter.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Redirect unauthenticated users to the login page.</li>
 *   <li>Allow public pages (login/register) and static assets (css/js/images).</li>
 *   <li>Restrict a small set of routes to the "Stock Manager" role.</li>
 * </ul>
 *
 * <p>Session attributes used:</p>
 * <ul>
 *   <li>{@code user} - the logged-in {@code com.inventrack.model.User}</li>
 *   <li>{@code role} - the role name string (e.g. Admin, Cashier, Stock Manager)</li>
 * </ul>
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Step 1: Cast to HTTP types (this filter is mapped for HTTP traffic).
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Step 2: Determine request path and context for routing checks.
        String path = req.getRequestURI();
        String contextPath = req.getContextPath();

        // Step 3: Compute the basic allow/deny conditions for this request.
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = path.endsWith("login.jsp") || path.endsWith("/login") || path.endsWith("/register") || path.endsWith("register.jsp");
        boolean staticResource = path.startsWith(contextPath + "/css/") || path.startsWith(contextPath + "/js/") || path.startsWith(contextPath + "/images/");
        boolean stockManagerOnlyPath = path.startsWith(contextPath + "/categories") || path.startsWith(contextPath + "/suppliers");

        // Step 4: Force unauthenticated users to sign in (except for public/static endpoints).
        if (!loggedIn && !loginRequest && !staticResource) {
            res.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Step 5: Enforce role-based access for Stock Manager-only features.
        if (loggedIn && stockManagerOnlyPath) {
            HttpSession currentSession = req.getSession(false);
            Object roleObj = currentSession != null ? currentSession.getAttribute("role") : null;
            String role = roleObj != null ? roleObj.toString() : "";
            if (!"Stock Manager".equalsIgnoreCase(role)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can access this feature.");
                return;
            }
        }

        // Step 6: Continue processing in the filter chain (servlet/JSP/etc.).
        chain.doFilter(request, response);
    }
}
