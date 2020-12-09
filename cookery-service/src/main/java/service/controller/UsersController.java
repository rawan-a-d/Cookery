package service.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.UsersRepository;

import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class UsersController {
    private final static Logger LOGGER = Logger.getLogger(UsersController.class.getName());

//    @Inject
    UsersRepository usersRepository = new UsersRepository();

    //	------------------------------------------------------------------------ Users ------------------------------------------------------------------------------
    public List<User> getUsers() {
        List<User> users;
        try {
            users = usersRepository.getUsers();
            return users;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

            return null;
        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;

        }
    }


    public User getUser(int id) {
        User user;

        try {
            user = usersRepository.getUser(id);
            return user;
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }


    public boolean createUser(User user) {
        try {
            return usersRepository.createUser(user);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public boolean updateUser(int id, User user) {
        try {
            return usersRepository.updateUser(id, user);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }

    public boolean deleteUser(int id) {
        try {
            return usersRepository.deleteUser(id);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public static int getUserId(int recipeId) {
        UsersRepository usersRepository = new UsersRepository();

        int id = -1;
        try {
            id = usersRepository.getUserId(recipeId);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
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
}
