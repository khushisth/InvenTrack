package com.inventrack.service;

import java.util.List;

import com.inventrack.dao.UserDAO;
import com.inventrack.model.User;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean approveUser(int userId) {
        return userDAO.approveUser(userId);
    }
}