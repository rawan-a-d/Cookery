package repository;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.model.DTO.UserBase;
import service.model.DTO.UserDTO;
import service.model.DTO.UserFollowDTO;
import service.model.Role;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.JDBCRepository;
import service.repository.UsersRepository;

import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UsersRepositoryTest {

    @Mock
    JDBCRepository jdbcRepository;

    @InjectMocks
    UsersRepository usersRepository;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, CookeryDatabaseException, URISyntaxException {

        Class.forName ("org.h2.Driver");

        when(jdbcRepository.getDatabaseConnection()).thenReturn(
                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
        );

        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:data.sql", UTF8, false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:shutdown.sql", UTF8, false);
    }


    @Test
    public void getUsers() throws CookeryDatabaseException, URISyntaxException {
        List<UserDTO> users = usersRepository.getUsers();

        List<UserDTO> actualUsers = Arrays.asList(
                new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin),
                new UserDTO(2, "Anas", "anas@gmail.com", Role.user),
                new UserDTO(3, "Omar", "omar@gmail.com", Role.admin),
                new UserDTO(4, "Raneem", "raneem@gmail.com", Role.user)
        );

        assertEquals(4, users.size());

        assertArrayEquals(users.toArray(), actualUsers.toArray()); // in order to use this (equals need to be implemented in User)
    }

    @Test
    public void getUser() throws CookeryDatabaseException, URISyntaxException {
        UserDTO expectedUser = new UserDTO(1,"Rawan", "rawan@gmail.com", Role.admin);

        UserDTO actualUser = usersRepository.getUser(1);

        assertEquals(expectedUser, actualUser);
    }


    @Test
    public void getUser_invalidId_throwsException() {
        assertThrows(CookeryDatabaseException.class, () -> {
            usersRepository.getUser(6);
        });
    }


    @Test
    public void createUser() throws CookeryDatabaseException, URISyntaxException {
        UserDTO expectedUser = new UserDTO(5, "Beatrice", "beatrice@gmail.com", Role.user);

        UserDTO actualUser = usersRepository.createUser(new User("Beatrice", "beatrice@gmail.com", "Tr1234587@"));

        assertEquals(expectedUser, actualUser);
    }


    @Test
    public void deleteUser() throws CookeryDatabaseException, URISyntaxException {
        boolean result = usersRepository.deleteUser(1);

        assertTrue(result);
    }


    @Test
    public void updateUser() throws CookeryDatabaseException, URISyntaxException {
        User expectedUser = new User(1, "Rawan", "rawan.ad@gmail.com", "1234", Role.admin);

        boolean result = usersRepository.updateUser(1, expectedUser);

        assertTrue(result);
    }


    @Test
    public void updateUser_invalidId_throwsException() {
        assertThrows(CookeryDatabaseException.class, () -> {
            usersRepository.updateUser(6, new User("Beatrice", "beatrice@gmail.com", "Tr1234587@"));
        });
    }


    @Test
    public void follow() throws CookeryDatabaseException {
        UserDTO followee = new UserDTO(3, "Omar");

        int followId = usersRepository.follow(1, followee);

        assertEquals(5, followId);
    }


    @Test
    public void unFollow() throws CookeryDatabaseException {
        boolean result = usersRepository.unFollow(1, 3);

        assertTrue(result);
    }


    @Test
    public void getFollowers() throws CookeryDatabaseException {
        List<UserFollowDTO> expectedUsers = Arrays.asList(
                new UserFollowDTO(1, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                new UserFollowDTO(2, new UserBase(3, "Omar", "omar@gmail.com"), "omar image")
        );

        List<UserFollowDTO> actualUsers = usersRepository.getFollowers(1);


        assertEquals(expectedUsers.size(), actualUsers.size());

        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray()); // in order to use this (equals need to be implemented in User)
    }

    @Test
    public void getFollowees() throws CookeryDatabaseException {
        List<UserFollowDTO> expectedUsers = Arrays.asList(
                new UserFollowDTO(3, new UserBase(2, "Anas", "anas@gmail.com"), "anas image"),
                new UserFollowDTO(4, new UserBase(4, "Raneem", "raneem@gmail.com"), "raneem image")
        );

        List<UserFollowDTO> actualUsers = usersRepository.getFollowees(1);

        assertEquals(expectedUsers.size(), actualUsers.size());

        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray()); // in order to use this (equals need to be implemented in User)
    }
}
