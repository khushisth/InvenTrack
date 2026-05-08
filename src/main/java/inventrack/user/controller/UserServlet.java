package inventrack.user.controller;

import java.util.List;
import inventrack.service.UserService;
import inventrack.user.model.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null ) {
            resp.sendRedirect(req.getContextPath() + "/login");
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equalsIgnoreCase(currentUser.getRoleName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users );
        req.getRequestDispatcher("/user.jsp").forward(req, resp);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"Admin".equalsIgnoreCase(currentUser.getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

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

        // Reload the user list
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

}
