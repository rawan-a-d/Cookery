package service;

import service.model.Recipe;
import service.model.User;
import service.repository.AuthenticationRepository;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;
import service.repository.UsersRepository;

import java.util.List;

public class Controller {
	// ---------------------------------------------------- Recipes ------------------------------------------------------------
	public List<Recipe> getRecipes() {
		RecipesRepository recipesRepository = new RecipesRepository();


		System.out.println("Recipes:");
		List<Recipe> recipes;
		try {
			recipes = recipesRepository.getRecipes();
//			for (Recipe recipe: recipes) {
//				System.out.println("\t" + recipe);
//			}
			return recipes;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Recipe getRecipe(int id) {
		RecipesRepository recipesRepository = new RecipesRepository();

		Recipe recipe;
		try {
			recipe = recipesRepository.getRecipe(id);
			return recipe;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<Recipe> getRecipes(int userId) {
		RecipesRepository recipesRepository = new RecipesRepository();

		System.out.println("Recipes:");
		List<Recipe> recipes;
		try {
			recipes = recipesRepository.getRecipes(userId);
			return recipes;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<Recipe> getRecipes(String ingredient) {
		RecipesRepository recipesRepository = new RecipesRepository();

		List<Recipe> recipes;
		try {
			recipes = recipesRepository.getRecipes(ingredient);

			return recipes;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}




	public boolean updateRecipe(int id, Recipe recipe) {
		RecipesRepository recipesRepository = new RecipesRepository();

		try {
			return recipesRepository.updateRecipe(id, recipe);
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void createRecipe(Recipe recipe) {
		RecipesRepository recipesRepository = new RecipesRepository();

		try {
			recipesRepository.createRecipe(recipe);
		}
		catch (CookeryDatabaseException e) {
			e.printStackTrace();
		}
	}

	public void deleteRecipe(int id) {
		RecipesRepository recipesRepository = new RecipesRepository();

		try {
			recipesRepository.deleteRecipe(id);
		}
		catch (CookeryDatabaseException e) {
			e.printStackTrace();
		}
	}


	//	------------------------------------------------------------------------ Users ------------------------------------------------------------------------------
	public List<User> getUsers() {
		UsersRepository usersRepository = new UsersRepository();

		List<User> users;
		try {
			users = usersRepository.getUsers();
//			for (Recipe recipe: recipes) {
//				System.out.println("\t" + recipe);
//			}
			return users;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public User getUser(int id) {
		UsersRepository usersRepository = new UsersRepository();

		User user;
		try {
			user = usersRepository.getUser(id);
//			for (Recipe recipe: recipes) {
//				System.out.println("\t" + recipe);
//			}
			return user;
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public boolean createUser(User user) {
		UsersRepository usersRepository = new UsersRepository();

		try {
			return usersRepository.createUser(user);
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return false;
		}
	}


	public boolean updateUser(int id, User user) {
		UsersRepository usersRepository = new UsersRepository();

		try {
			return usersRepository.updateUser(id, user);
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void deleteUser(int id) {
		UsersRepository usersRepository = new UsersRepository();

		try {
			usersRepository.deleteUser(id);
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
		}
	}


	/*---------------------------------------------------------------- Authenticate ----------------------------------------------------------------------*/
	public User authenticate(String email, String password) {
		AuthenticationRepository authenticationRepository = new AuthenticationRepository();

		User user = null;
		try {
			 user = authenticationRepository.authenticate(email, password);
		}
		catch (CookeryDatabaseException ex) {
			ex.printStackTrace();
		}

		return user;
	}

}