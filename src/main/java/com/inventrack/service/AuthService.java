package com.inventrack.service;

import com.inventrack.dao.UserDAO;
import com.inventrack.model.User;
import com.inventrack.util.PasswordUtil;

/**
 * Authentication-related business logic.
 *
 * <p>This service encapsulates:</p>
 * <ul>
 *   <li>Login: credential verification + approval rules</li>
 *   <li>Registration: password hashing + default flags</li>
 * </ul>
 */
public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Attempts to authenticate a user.
     *
     * <p>Rules:</p>
     * <ul>
     *   <li>User must exist and be active.</li>
     *   <li>Password must match the stored BCrypt hash.</li>
     *   <li>Non-admin users must be approved by an admin.</li>
     * </ul>
     *
     * @param username username (unique)
     * @param password plain-text password from the login form
     * @return the authenticated {@link User}, or {@code null} if credentials are invalid
     * @throws RuntimeException when the account is pending admin approval
     */
    public User login(String username, String password) {
        // Step 1: Load the user record (includes role name via join).
        User user = userDAO.getUserByUsername(username);
        
        // Step 2: Verify account state and password.
        if (user != null && user.isActive()) {
            if (PasswordUtil.checkPassword(password, user.getPassword())) {
                // Step 3: Enforce approval for non-admin roles.
                if (!user.getRoleName().equalsIgnoreCase("Admin") && !user.isApproved()) {
                    throw new RuntimeException("Account pending admin approval.");
                }
                // Step 4: Successful login.
                return user;
            }
        }
        return null;
    }

    /**
     * Registers a new user account.
     *
     * <p>The password is hashed prior to persistence. The DAO sets {@code is_approved} based on role.</p>
     *
     * @param user user details from the registration form
     * @return true if the record was created; false otherwise
     */
    public boolean register(User user) {
        // Step 1: Hash the password before saving it.
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        // Step 2: Mark active by default; approval is handled in the DAO based on role.
        user.setActive(true);
        
        // Step 3: Persist.
        return userDAO.registerUser(user);
    }
}
