package inventrack.user.model.dao;

import inventrack.user.model.User;

public interface UserInterface {
    boolean registerUser(User user);
    User loginUser(String email, String password);
}
