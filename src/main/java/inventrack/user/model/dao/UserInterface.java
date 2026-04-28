package inventrack.user.model.dao;

import inventrack.user.model.User;
import java.util.List;

public interface UserInterface {
    boolean registerUser(User user);
    User loginUser(String email, String password);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean approveUser(int userId);
}
