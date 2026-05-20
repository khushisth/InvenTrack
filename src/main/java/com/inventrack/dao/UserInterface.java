package com.inventrack.dao;

import java.util.List;

import com.inventrack.model.User;

public interface UserInterface {
    boolean registerUser(User user);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean approveUser(int userId);
}
