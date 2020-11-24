package service.repository;

import service.model.Role;
import service.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationRepository extends JDBCRepository {
    public User authenticate(String email, String password) throws CookeryDatabaseException {
        Connection connection = super.getDatabaseConnection();
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()) {
                connection.close();
                throw new CookeryDatabaseException("Email or password are incorrect");
            }
            else {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                Role role = Role.valueOf(resultSet.getString("role"));

                connection.close();

                return new User(id, name, userEmail, userPassword, role);
            }
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot delete favourite from the database", throwable);
        }
    }


//    public boolean isOwner(int tokenUserId, int userId) throws CookeryDatabaseException{
//        Connection connection = super.getDatabaseConnection();
//        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, email);
//            statement.setString(2, password);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if(!resultSet.next()) {
//                connection.close();
//                throw new CookeryDatabaseException("Email or password are incorrect");
//            }
//            else {
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                String userEmail = resultSet.getString("email");
//                String userPassword = resultSet.getString("password");
//                Role role = Role.valueOf(resultSet.getString("role"));
//
//                connection.close();
//
//                return new User(id, name, userEmail, userPassword, role);
//            }
//        }
//        catch (SQLException throwable) {
//            throw new CookeryDatabaseException("Cannot delete favourite from the database", throwable);
//        }
    }
