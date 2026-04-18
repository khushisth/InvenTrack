package inventrack.user.model.dao;

import inventrack.user.model.User;

public class UserDAO implements UserInterface{
    @Override
    public boolean registerUser(User user) {
        return false;
    }

    @Override
    public User loginUser(String email, String password) {
        return null;
    }
}
