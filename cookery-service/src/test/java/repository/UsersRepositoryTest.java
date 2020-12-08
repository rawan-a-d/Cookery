package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.model.Role;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.JDBCRepository;
import service.repository.UsersRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UsersRepositoryTest {

    @Mock
    JDBCRepository jdbcRepository;

    @InjectMocks
    UsersRepository usersRepository;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, CookeryDatabaseException {

        Class.forName ("org.h2.Driver");

        when(jdbcRepository.getDatabaseConnection()).thenReturn(

//                DriverManager.getConnection ("jdbc:h2:~/test") // WORKING

                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
//                DriverManager.getConnection("jdbc:h2:mem:test_mem", "root", "")
//                DriverManager.getConnection("jdbc:h2:mem:test_mem;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", "", "") // select * from SCHEMA_COOKERY.recipe;
//                DriverManager.getConnection("jdbc:h2:mem:;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false") // select * from SCHEMA_COOKERY.recipe;
        // jdbc:h2:mem:~/test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false
        );

        System.out.println("JDBC " + jdbcRepository.getDatabaseConnection());

        repository.JDBCRepository.generateData();
    }

    @Test
    public void getUsers() throws CookeryDatabaseException, SQLException, ClassNotFoundException {
        List<User> users = usersRepository.getUsers();

        List<User> actualUsers = Arrays.asList(
                new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin),
                new User(2, "Anas", "anas@gmail.com", "1234", Role.user),
                new User(3, "Omar", "omar@gmail.com", "1234", Role.admin),
                new User(4, "Raneem", "raneem@gmail.com", "1234", Role.user)
        );

        assertEquals(4, users.size());

        assertArrayEquals(users.toArray(), actualUsers.toArray()); // in order to use this (equals need to be implemented in User)
    }

    @Test
    public void getUser() throws CookeryDatabaseException {
        User expectedUser = new User(1,"Rawan", "rawan@gmail.com", "1234", Role.admin);

        User actualUser = usersRepository.getUser(1);

        assertEquals(expectedUser, actualUser);
    }


    @Test
    public void getUser_invalidId_throwsException() {
        assertThrows(CookeryDatabaseException.class, () -> {
            usersRepository.getUser(6);
        });
    }


    @Test
    public void createUser() throws CookeryDatabaseException {
        boolean result = usersRepository.createUser(new User("Beatrice", "beatrice@gmail.com", "1234"));

        assertTrue(result);
    }


    @Test
    public void deleteUser() throws CookeryDatabaseException {
        boolean result = usersRepository.deleteUser(1);

        assertTrue(result);
    }


    @Test
    public void updateUser() throws CookeryDatabaseException {
        User expectedUser = new User(1, "Rawan", "rawan.ad@gmail.com", "1234", Role.admin);

        boolean result = usersRepository.updateUser(1, expectedUser);

        assertTrue(result);
    }

    @Test
    public void updateUser_invalidId_throwsException() {
        assertThrows(CookeryDatabaseException.class, () -> {
            usersRepository.updateUser(6, new User("Beatrice", "beatrice@gmail.com", "1234"));
        });
    }

}
