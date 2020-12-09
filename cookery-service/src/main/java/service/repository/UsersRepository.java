package service.repository;


import service.model.Role;
import service.model.User;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository extends JDBCRepository{

//	@Inject
	JDBCRepository jdbcRepository;

	public UsersRepository() {
		this.jdbcRepository = new JDBCRepository();
	}

	public List<User> getUsers() throws CookeryDatabaseException, URISyntaxException {
		List<User> users = new ArrayList<>();

		String sql = "SELECT * FROM user";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				Role role = Role.valueOf(resultSet.getString("role"));


				User user = new User(id, name, email, password, role);
				users.add(user);
			}

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
		}

		return users;
	}


	public User getUser(int id) throws CookeryDatabaseException, URISyntaxException {
		String sql = "SELECT * FROM user WHERE id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				throw new CookeryDatabaseException("User with id " + id + " cannot be found");
			} else {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				Role role = Role.valueOf(resultSet.getString("role"));

				User user = new User(id, name, email, password, role);

				return user;
			}

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}

	public int getUserId(int recipeId) throws CookeryDatabaseException, URISyntaxException {
		String sql = "SELECT * FROM user INNER JOIN recipe ON recipe.id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, recipeId);
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				throw new CookeryDatabaseException("User with recipe id " + recipeId + " cannot be found");
			} else {
				int id = resultSet.getInt("user_id");

				return id;
			}

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}


	public boolean createUser(User user) throws CookeryDatabaseException, URISyntaxException {
		String sql = "INSERT INTO user (name, email, password, role) VALUES (?, ?, ?, ?)";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			if (user.getRole() == null) {
				user.setRole(Role.user);
			}
			statement.setString(4, user.getRole().toString());

			statement.executeUpdate();

			connection.commit();

			return true;
		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot create user in the database", throwable);
		}
	}

	public boolean deleteUser(int id) throws CookeryDatabaseException, URISyntaxException {
		String sql = "DELETE FROM user WHERE id = ?";
		String deleteRecipesSql = "DELETE FROM recipe WHERE id = ?";
		String deleteIngredientsSql = "DELETE FROM ingredient WHERE recipe_id = ?";
		String deleteFavouritesSql = "DELETE FROM user_favourite_recipe WHERE user_id = ?";
		String deleteFavouritesByOtherSql = "DELETE FROM user_favourite_recipe WHERE recipe_id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql);
			PreparedStatement deleteRecipesStatement = connection.prepareStatement(deleteRecipesSql);
			PreparedStatement deleteIngredientsStatement = connection.prepareStatement(deleteIngredientsSql);
			PreparedStatement deleteFavouritesStatement = connection.prepareStatement(deleteFavouritesSql);
			PreparedStatement deleteFavouritesByOtherStatement = connection.prepareStatement(deleteFavouritesByOtherSql)) {


			// Get all recipes
			String recipesSql = "SELECT id, user_id FROM recipe WHERE user_id = ?";
			PreparedStatement recipesStatement = connection.prepareStatement(recipesSql);
			recipesStatement.setInt(1, id);
			ResultSet resultSet = recipesStatement.executeQuery();

			while (resultSet.next()) {
				int recipeId = resultSet.getInt("id");

				// delete favourites
				deleteFavouritesStatement.setInt(1, id);
				deleteFavouritesStatement.executeUpdate();

				deleteFavouritesByOtherStatement.setInt(1, recipeId);
				deleteFavouritesByOtherStatement.executeUpdate();

				// delete ingredients
				deleteIngredientsStatement.setInt(1, recipeId);
				deleteIngredientsStatement.executeUpdate();

				// delete recipe
				deleteRecipesStatement.setInt(1, recipeId);
				deleteRecipesStatement.executeUpdate();
			}

			// delete user
			statement.setInt(1, id);
			statement.executeUpdate();

			connection.commit();

			return true;

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot delete user in the database", throwable);
		}
	}

	public boolean updateUser(int id, User user) throws CookeryDatabaseException, URISyntaxException {
		String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setInt(4, id);

			int affected = statement.executeUpdate();

			if(affected <= 0) {
				connection.close();
				throw new CookeryDatabaseException("User was not found");
			}
			connection.commit();

			return true;

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot update user in the database", throwable);
		}
	}
}
