package inventrack.user.model.dao;

import inventrack.user.model.User;

public class UserDAO implements UserInterface{
    @Override
    public boolean registerUser(User user) {
        if(user.getEmail().trim().isEmpty() || user.getPassword().trim().isEmpty()){
            return false;
        }
        String sql = "INSER INTO user(name,email,password,role) values(?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1,user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getRole());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loginUser(String email, String password) {
        return null;
    }
}
