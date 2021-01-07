package service.repository;


import service.model.DTO.ProfileDTO;
import service.model.DTO.UserBase;
import service.model.DTO.UserDTO;
import service.model.Role;
import service.model.User;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository extends JDBCRepository{

//	@Inject
	JDBCRepository jdbcRepository;

	public UsersRepository() {
		this.jdbcRepository = new JDBCRepository();
	}

	public List<UserDTO> getUsers() throws CookeryDatabaseException, URISyntaxException {
		List<UserDTO> users = new ArrayList<>();

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


				UserDTO user = new UserDTO(id, name, email, role);
				users.add(user);
			}

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
		}

		return users;
	}


	public UserDTO getUser(int id) throws CookeryDatabaseException, URISyntaxException {
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
				Role role = Role.valueOf(resultSet.getString("role"));

				UserDTO user = new UserDTO(id, name, email, role);

				return user;
			}

		} catch (SQLException throwable) {
			throw new CookeryDatabaseException("Cannot read user from the database", throwable);
		}
	}

	public ProfileDTO getProfile(int userId) throws CookeryDatabaseException, URISyntaxException {
		String sql = "SELECT user.id, user.name, user.email, user.image, " +
						"(SELECT COUNT(recipe.user_id) from recipe where user_id = user.id) AS recipesNr, " +
						"(SELECT COUNT(follow.followee_id) from follow WHERE followee_id = user.id) AS followersNr " +
						"FROM user " +
						"WHERE user.id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				throw new CookeryDatabaseException("User with id " + userId + " cannot be found");
			} else {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String image = resultSet.getString("image");
				int recipesNr = resultSet.getInt("recipesNr");
				int followersNr = resultSet.getInt("followersNr");

				UserBase user = new UserBase(userId, name, email);
				ProfileDTO profile = new ProfileDTO(user, image, recipesNr, followersNr);

				return profile;
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


	public UserDTO createUser(User user) throws CookeryDatabaseException, URISyntaxException {
		String sql = "INSERT INTO user (name, email, password, role, image) VALUES (?, ?, ?, ?, ?)";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			// Encrypt password
			statement.setString(3, doHashing(user.getPassword()));
			statement.setString(4, Role.user.toString());
			statement.setString(5, "placeholder.png");

			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			int userId = -1;
			if(resultSet.next()) {
				userId = resultSet.getInt(1);
			}
			else {
				throw new CookeryDatabaseException("Cannot get the id of the new user.");
			}

			return new UserDTO(userId, user.getName(), user.getEmail(), Role.user);
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
		String recipesSql = "SELECT id, user_id FROM recipe WHERE user_id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql);
			PreparedStatement deleteRecipesStatement = connection.prepareStatement(deleteRecipesSql);
			PreparedStatement deleteIngredientsStatement = connection.prepareStatement(deleteIngredientsSql);
			PreparedStatement deleteFavouritesStatement = connection.prepareStatement(deleteFavouritesSql);
			PreparedStatement deleteFavouritesByOtherStatement = connection.prepareStatement(deleteFavouritesByOtherSql);
			 PreparedStatement recipesStatement = connection.prepareStatement(recipesSql);) {


			// Get all recipes
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
		// Update columns if not null, if null keep old data
		String sql = "UPDATE user SET name = IFNULL(?, name), " +
				"email = IFNULL(?, email), " +
				"password = IFNULL(?, password), " +
				"role = IFNULL(?, role) " +
				"WHERE id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			String role = user.getRole() == null ? null : user.getRole().toString();
			statement.setString(4, role);
			statement.setInt(5, id);

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


	public int follow(int followerId, UserDTO followee) throws CookeryDatabaseException { // could be current user id, followee object (User)
		String sql = "INSERT INTO follow (follower_id, followee_id) VALUES (?, ?)";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, followerId);
			statement.setInt(2, followee.getId());

			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();

			if(resultSet.next()) {
				int followId = resultSet.getInt(1);

				connection.commit();

				return followId;
			}
			else {
				throw new CookeryDatabaseException("Cannot get the id of the new recipe.");
			}

		} catch (SQLException | URISyntaxException throwable) {
			throw new CookeryDatabaseException("Cannot follow user", throwable);
		}
	}


	public boolean unFollow(int followerId, int followId) throws CookeryDatabaseException { // could be current user id, followee object (User)
		String sql = "DELETE FROM follow WHERE id = ? AND follower_id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, followId);
			statement.setInt(2, followerId);

			statement.executeUpdate();

			connection.commit();

			return true;
		} catch (SQLException | URISyntaxException throwable) {
			throw new CookeryDatabaseException("Cannot unfollow user", throwable);
		}
	}


	//To encrypt the password
	public static String doHashing (String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.update(password.getBytes());

			byte[] resultByteArray = messageDigest.digest();

			StringBuilder sb = new StringBuilder();

			for (byte b : resultByteArray) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	public boolean uploadImage(int id, String name) throws CookeryDatabaseException {
		String sql = "UPDATE user SET image = ? WHERE id = ?";

		try (Connection connection = jdbcRepository.getDatabaseConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, name);
			statement.setInt(2, id);

			int affected = statement.executeUpdate();

			if(affected <= 0) {
				connection.close();
				throw new CookeryDatabaseException("User was not found");
			}
			connection.commit();

			return true;

		} catch (SQLException | URISyntaxException throwable) {
			throw new CookeryDatabaseException("Cannot update user in the database", throwable);
		}
	}


	// Followers by userId
	// return list of follows (id, userDTO)
//	public List<> getFollowers(int id) {
//
//	}
}
