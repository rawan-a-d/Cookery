package service.repository;


import service.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository extends JDBCRepository  {

	public List<User> getUsers() throws CookeryDatabaseException {
		List<User> users = new ArrayList<>();

		Connection connection = super.getDatabaseConnection();
		String sql = "SELECT * FROM user";

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");

				User user = new User(id, name, email, password);
				users.add(user);
			}

			connection.close();
		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
		}

		return users;
	}


	public User getUser(int id) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "SELECT * FROM user WHERE id = ?";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if(!resultSet.next()) {
				connection.close();
				throw new CookeryDatabaseException("User wuth id " + id + " cannot be found");
			}
			else {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");

				connection.close();

				return new User(id, name, email, password);
			}

		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}


	public boolean createUser(User user) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());

			statement.executeUpdate();

			connection.commit();
			connection.close();

			return true;
		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}

	public boolean deleteUser(int id) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "DELETE FROM user WHERE id = ?";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();

			connection.commit();
			connection.close();

			return true;

		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}

	public boolean updateUser(int id, User user) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setInt(4, id);

			statement.executeUpdate();

			connection.commit();
			connection.close();

			return true;

		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}

	public void addToFavourite(int userId, int recipeId) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (?, ?)";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, recipeId);

			statement.executeUpdate();

			connection.commit();
			connection.close();
		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot insert favourite into the database", throwable);
		}
	}

	public void removeFromFavourite(int userId, int recipeId) throws CookeryDatabaseException {
		Connection connection = super.getDatabaseConnection();
		String sql = "DELETE FROM user_favourite_recipe WHERE user_id = ? AND recipe_id = ?";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, recipeId);

			statement.executeUpdate();

			connection.commit();
			connection.close();
		}
		catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot delete favourite from the database", throwable);
		}
	}
}
