package service.repository;

import service.model.Role;
import service.model.User;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthRepository extends JDBCRepository {
    public User authenticate(String email, String password) throws CookeryDatabaseException, URISyntaxException {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (Connection connection = super.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
            throw new CookeryDatabaseException("Cannot authenticate user", throwable);
        }
    }

}
