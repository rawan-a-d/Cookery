package service.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import service.model.DTO.RecipeDTO;
import service.model.Recipe;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;
import service.repository.UsersRepository;

import java.util.List;
import java.util.logging.Logger;

public class UsersController {
    private final static Logger LOGGER = Logger.getLogger(UsersController.class.getName());

    //	------------------------------------------------------------------------ Users ------------------------------------------------------------------------------
    public List<User> getUsers() {

        UsersRepository usersRepository = new UsersRepository();

        List<User> users;
        try {
            users = usersRepository.getUsers();
            return users;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

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
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }


    public boolean createUser(User user) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            return usersRepository.createUser(user);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public boolean updateUser(int id, User user) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            return usersRepository.updateUser(id, user);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }

    public void deleteUser(int id) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            usersRepository.deleteUser(id);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
    }


    public int getUserId(int recipeId) {
        UsersRepository usersRepository = new UsersRepository();

        int id = -1;
        try {
            id = usersRepository.getUserId(recipeId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return id;
    }

//	public boolean isOwner(int tokenUserId, int userId) {
//		AuthenticationRepository authenticationRepository = new AuthenticationRepository();
//
//		boolean isOwner = tokenUserId == userId ? true : false;
//
//		return isOwner;
//	}


    public List<RecipeDTO> getRecipesDTO(int userId) {
        RecipesRepository recipesRepository = new RecipesRepository();

        List<RecipeDTO> recipes;
        try {
            recipes = recipesRepository.getRecipesDTO(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }




    public boolean addFavourite(int userId, RecipeDTO favourite) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            return usersRepository.addFavourite(userId, favourite);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public void removeFavourite(int favouriteId) {
        UsersRepository usersRepository = new UsersRepository();

        try {
            usersRepository.removeFavourite(favouriteId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
    }


    public List<Recipe> getFavourites(int userId) {
        UsersRepository usersRepository = new UsersRepository();

        List<Recipe> recipes;
        try {
            recipes = usersRepository.getFavourites(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }
}
