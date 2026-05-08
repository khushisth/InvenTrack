package inventrack.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();
        String contextPath = req.getContextPath();

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = path.endsWith("login.jsp") || path.endsWith("/login") || path.endsWith("/register") || path.endsWith("register.jsp");
        boolean staticResource = path.startsWith(contextPath + "/css/") || path.startsWith(contextPath + "/js/") || path.startsWith(contextPath + "/images/");
        boolean stockManagerOnlyPath = path.startsWith(contextPath + "/categories") || path.startsWith(contextPath + "/suppliers");

        if (!loggedIn && !loginRequest && !staticResource) {
            res.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        if (loggedIn && stockManagerOnlyPath) {
            HttpSession currentSession = req.getSession(false);
            Object roleObj = currentSession != null ? currentSession.getAttribute("role") : null;
            String role = roleObj != null ? roleObj.toString() : "";
            if (!"Stock Manager".equalsIgnoreCase(role)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Stock Manager can access this feature.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}

