package service.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import service.model.DTO.ProfileDTO;
import service.model.DTO.UserDTO;
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
    public List<UserDTO> getUsers() {
        List<UserDTO> users;
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


    public UserDTO getUser(int id) {
        UserDTO user;

        try {
            user = usersRepository.getUser(id);
            return user;
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }


    public UserDTO createUser(User user) {
        UserDTO userDTO = null;
        try {
            userDTO = usersRepository.createUser(user);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return userDTO;
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

    // 	public boolean follow(int followerId, UserDTO followee) throws CookeryDatabaseException { // could be current user id, followee object (User)
    public int follow(int followerId, UserDTO followee) {
        int result = -1;
        try {
            result = usersRepository.follow(followerId, followee);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }



    public boolean unFollow(int followerId, int followId) {
        boolean result = false;
        try {
            result = usersRepository.unFollow(followerId, followId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }


    public ProfileDTO getProfile(int userId) {
        ProfileDTO profile = null;
        try {
            profile = usersRepository.getProfile(userId);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return profile;
    }


    public boolean uploadImage(int id, String name) {
        boolean result = false;
        try {
            result = usersRepository.uploadImage(id, name);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }
}
