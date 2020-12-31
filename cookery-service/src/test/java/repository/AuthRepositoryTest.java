package repository;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.model.DTO.UserDTO;
import service.model.Role;
import service.repository.AuthRepository;
import service.repository.CookeryDatabaseException;

import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthRepositoryTest {

    @Mock
    service.repository.JDBCRepository jdbcRepository;

    @InjectMocks
    AuthRepository authRepository;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, URISyntaxException {
        Class.forName ("org.h2.Driver");

        when(jdbcRepository.getDatabaseConnection()).thenReturn(
                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
        );

        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:data.sql", UTF8, false);
    }


    @Test
    public void authenticate() throws URISyntaxException, CookeryDatabaseException {
        UserDTO expectedUser = new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin);

        UserDTO actualUser = authRepository.authenticate("rawan@gmail.com", "cd73952c896e75f83a188d4d16858ef2");

        assertEquals(expectedUser, actualUser);
    }


    @Test
    public void authenticate_wrongCredentials() {
        assertThrows(CookeryDatabaseException.class, () -> {
            authRepository.authenticate("denys@gmail.com", "12345");
        });
    }
}
