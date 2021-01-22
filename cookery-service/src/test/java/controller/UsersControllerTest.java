package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.UsersController;
import service.model.DTO.UserBase;
import service.model.DTO.UserDTO;
import service.model.DTO.UserFollowDTO;
import service.model.Role;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.UsersRepository;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @InjectMocks
    UsersController usersController;

    @Mock
    UsersRepository usersRepository;

    @Test
    public void getUsers() throws CookeryDatabaseException, URISyntaxException {
            List<UserDTO> expectedUsers = Arrays.asList(
                    new UserDTO(1, "Anas", "anas@gmail.com", Role.user),
                    new UserDTO(2, "Beatrice", "beatrice@gmail.com", Role.admin)
            );

            when(usersRepository.getUsers()).thenReturn(
                    Arrays.asList(
                            new UserDTO(1, "Anas", "anas@gmail.com", Role.user),
                            new UserDTO(2, "Beatrice", "beatrice@gmail.com", Role.admin)
                    )
            );

            List<UserDTO> actualUsers = usersController.getUsers();

            assertEquals(expectedUsers, actualUsers);
            assertEquals(expectedUsers.size(), actualUsers.size());
    }


    @Test
    public void getUser() throws CookeryDatabaseException, URISyntaxException {
        when(usersRepository.getUser(1)).thenReturn(
                new UserDTO(1, "Anas", "anas@gmail.com", Role.user)
        );

        UserDTO user = usersController.getUser(1);

        assertNotEquals(null, user);
        assertEquals(1, user.getId());
        assertEquals("Anas", user.getName());
        assertEquals("anas@gmail.com", user.getEmail());
    }

    @Test
    public void getUser_invalidId_returnNull() throws CookeryDatabaseException, URISyntaxException {
        when(usersRepository.getUser(4)).thenReturn(
                null
        );

        UserDTO user = usersController.getUser(4);

        assertEquals(null, user);
    }


    @Test
    public void createUser() throws CookeryDatabaseException, URISyntaxException {
        User user = new User(1, "Omar", "omar@gmail.com", "1234", Role.user);

        UserDTO expectedUser = new UserDTO(1, "Omar", "omar@gmail.com", Role.user);
        when(usersRepository.createUser(user)).thenReturn(
                expectedUser
        );

        assertEquals(expectedUser, usersController.createUser(user));
    }


    @Test
    public void updateUser() throws CookeryDatabaseException, URISyntaxException {
        User user = new User(1, "Omar", "omar@gmail.com", "1234", Role.user);
        when(usersRepository.updateUser(1, user)).thenReturn(true);

        assertTrue(usersController.updateUser(1, user));
    }


    @Test
    public void deleteUser() throws CookeryDatabaseException, URISyntaxException {
        when(usersRepository.deleteUser(1)).thenReturn(true);

        assertTrue(usersController.deleteUser(1));
    }


    @Test
    public void follow() throws CookeryDatabaseException {
        UserDTO followee = new UserDTO(3, "Omar");

        when(usersRepository.follow(1, followee)).thenReturn(5);

        assertEquals(5, usersController.follow(1, followee));
    }


    @Test
    public void unFollow() throws CookeryDatabaseException {
        when(usersRepository.unFollow(1, 3)).thenReturn(true);

        assertTrue(usersController.unFollow(1, 3));
    }


    @Test
    public void getFollowers() throws CookeryDatabaseException {
        List<UserFollowDTO> expectedUsers = Arrays.asList(
                new UserFollowDTO(1, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                new UserFollowDTO(2, new UserBase(3, "Omar", "omar@gmail.com"), "omar image")
        );

        when(usersRepository.getFollowers(1)).thenReturn(
                Arrays.asList(
                        new UserFollowDTO(1, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                        new UserFollowDTO(2, new UserBase(3, "Omar", "omar@gmail.com"), "omar image")
                )
        );

        List<UserFollowDTO> actualUsers = usersController.getFollowers(1);

        assertEquals(expectedUsers, actualUsers);
        assertEquals(expectedUsers.size(), actualUsers.size());
    }

    @Test
    public void getFollowees() throws CookeryDatabaseException {
        List<UserFollowDTO> expectedUsers = Arrays.asList(
                new UserFollowDTO(3, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                new UserFollowDTO(4, new UserBase(4, "Raneem", "raneem@gmail.com"), "raneem image")
        );

        when(usersRepository.getFollowees(1)).thenReturn(
                Arrays.asList(
                        new UserFollowDTO(3, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                        new UserFollowDTO(4, new UserBase(4, "Raneem", "raneem@gmail.com"), "raneem image")
                )
        );

        List<UserFollowDTO> actualUsers = usersController.getFollowees(1);

        assertEquals(expectedUsers, actualUsers);
        assertEquals(expectedUsers.size(), actualUsers.size());
    }
}
