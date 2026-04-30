package inventrack.service;

import inventrack.user.model.User;
import inventrack.user.model.dao.UserDAO;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user != null && user.isActive()) {
            if (PasswordUtil.checkPassword(password, user.getPassword())) {
                // If the user is a Cashier or Stock Manager, check if they are approved by Admin
                if (!user.getRoleName().equalsIgnoreCase("Admin") && !user.isApproved()) {
                    throw new RuntimeException("Account pending admin approval.");
                }
                return user;
            }
        }
        return null;
    }

    public boolean register(User user) {
        // Hash the password before saving
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Active by default, but requires approval (handled in DAO)
        user.setActive(true);

        return userDAO.registerUser(user);
    }
}
