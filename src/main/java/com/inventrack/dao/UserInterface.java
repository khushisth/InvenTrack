package com.inventrack.dao;

import java.util.List;

import com.inventrack.model.User;

/**
 * Contract for basic user persistence operations.
 */
public interface UserInterface {
    /**
     * Creates a new user record.
     */
    boolean registerUser(User user);

    /**
     * Looks up a user by username (unique).
     */
    User getUserByUsername(String username);

    /**
     * @return all users
     */
    List<User> getAllUsers();

    /**
     * Marks a user as approved.
     */
    boolean approveUser(int userId);
}
