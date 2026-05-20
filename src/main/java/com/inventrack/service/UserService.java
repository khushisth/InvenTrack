package com.inventrack.service;

import java.util.List;

import com.inventrack.dao.UserDAO;
import com.inventrack.model.User;

/**
 * User administration business logic.
 *
 * <p>Used by the admin screen to list pending users and approve registrations.</p>
 */
public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * @return all users ordered by creation time (newest first)
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Marks a user as approved so they can log in (non-admin roles).
     *
     * @param userId user id to approve
     * @return true if the update succeeded
     */
    public boolean approveUser(int userId) {
        return userDAO.approveUser(userId);
    }
}
