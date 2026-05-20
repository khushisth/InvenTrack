package com.inventrack.controller;

import com.inventrack.model.Cart;
import com.inventrack.model.Product;
import com.inventrack.model.User;
import com.inventrack.service.OrderService;
import com.inventrack.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        if ("add".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            // Should optimize by adding a getProductById in DAO, but doing via search for simplicity
            Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId() == productId)
                .findFirst().orElse(null);
                
            if (product != null) {
                cart.addItem(product, quantity);
            }
        } else if ("remove".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            cart.removeItem(productId);
        } else if ("clear".equals(action)) {
            cart.clear();
        } else if ("checkout".equals(action)) {
            User user = (User) session.getAttribute("user");
            if (orderService.processCheckout(cart, user)) {
                // Do not clear cart yet, so receipt can print it
                request.getSession().setAttribute("checkoutSuccess", "Transaction completed successfully!");
                response.sendRedirect(request.getContextPath() + "/receipt.jsp");
                return;
            } else {
                request.getSession().setAttribute("checkoutError", "Transaction failed.");
            }
        }

        String returnTo = request.getParameter("returnTo");
        if ("cart".equals(returnTo)) {
            response.sendRedirect(request.getContextPath() + "/cart");
        } else if ("buyer-dashboard".equals(returnTo)) {
            response.sendRedirect(request.getContextPath() + "/buyer-dashboard");
        } else {
            User user = (User) session.getAttribute("user");
            if (user != null && "Buyer".equalsIgnoreCase(user.getRoleName())) {
                response.sendRedirect(request.getContextPath() + "/buyer-dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/pos");
            }
        }
    }
}
