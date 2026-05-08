package inventrack.service;

import inventrack.user.model.User;
import inventrack.user.model.dao.UserDAO;
import java.util.List;

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
