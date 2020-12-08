package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.UsersController;
import service.model.Role;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.UsersRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @InjectMocks
    UsersController usersController;

    @Mock
    UsersRepository usersRepository;

    @Test
    public void getUsers() throws CookeryDatabaseException {
            List<User> expectedUsers = Arrays.asList(
                    new User(1, "Anas", "anas@gmail.com", "1234", Role.user),
                    new User(2, "Beatrice", "beatrice@gmail.com", "1234", Role.admin)
            );

            when(usersRepository.getUsers()).thenReturn(
                    Arrays.asList(
                            new User(1, "Anas", "anas@gmail.com", "1234", Role.user),
                            new User(2, "Beatrice", "beatrice@gmail.com", "1234", Role.admin)
                    )
            );

            List<User> actualUsers = usersController.getUsers();

            assertEquals(expectedUsers, actualUsers);
            assertEquals(expectedUsers.size(), actualUsers.size());
    }


    @Test
    public void getUser() throws CookeryDatabaseException {
        when(usersRepository.getUser(1)).thenReturn(
                new User(1, "Anas", "anas@gmail.com", "1234", Role.user)
        );

        User user = usersController.getUser(1);

        assertNotEquals(null, user);
        assertEquals(1, user.getId());
        assertEquals("Anas", user.getName());
        assertEquals("anas@gmail.com", user.getEmail());
    }

    @Test
    public void getUser_invalidId_returnNull() throws CookeryDatabaseException {
        when(usersRepository.getUser(4)).thenReturn(
                null
        );

        User user = usersController.getUser(4);

        assertEquals(null, user);
    }


    @Test
    public void createUser() throws CookeryDatabaseException {
        User user = new User(1, "Omar", "omar@gmail.com", "1234", Role.user);
        when(usersRepository.createUser(user)).thenReturn(true);

        assertTrue(usersController.createUser(user));
    }


    @Test
    public void updateUser() throws CookeryDatabaseException {
        User user = new User(1, "Omar", "omar@gmail.com", "1234", Role.user);
        when(usersRepository.updateUser(1, user)).thenReturn(true);

        assertTrue(usersController.updateUser(1, user));
    }


    @Test
    public void deleteUser() throws CookeryDatabaseException {
        when(usersRepository.deleteUser(1)).thenReturn(true);

        assertTrue(usersController.deleteUser(1));
    }

//    @Test
//    public void getUserId() throws CookeryDatabaseException {
//        when(usersRepository.getUserId(2)).thenReturn(1);
//
//        assertEquals(UsersController.getUserId(2), 1);
//    }
}
