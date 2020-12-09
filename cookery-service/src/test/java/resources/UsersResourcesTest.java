package resources;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersResourcesTest extends JerseyTest {
//    @Mock
//    JDBCRepository jdbcRepository;
//
//    @BeforeEach
//    @Override
//    public void setUp() throws Exception {
//
//        Class.forName ("org.h2.Driver");
//
//        when(jdbcRepository.getDatabaseConnection()).thenReturn(
//                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
//        );
//
////        repository.JDBCRepository.generateData();
//
//        super.setUp();
//    }
//
//    @AfterEach
//    @Override
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
//
//    @Override
//    protected Application configure() {
//        return new ResourceConfig(UsersResources.class);
//    }
//
//
//
//
////    @Mock
////    service.repository.JDBCRepository jdbcRepository;
////
////
////    @BeforeEach
////    public void setUp() throws SQLException, ClassNotFoundException, CookeryDatabaseException, URISyntaxException {
////
////        Class.forName ("org.h2.Driver");
////
////        when(jdbcRepository.getDatabaseConnection()).thenReturn(
////                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
////        );
////
////        repository.JDBCRepository.generateData();
////    }
//
////    private static final URI BASE_URI = URI.create("http://localhost:90/");
//
//    @Test
//    public void getUser() {
//        //usersResources.getUser(1);
////        final User hello = target("users/1").request().get(User.class);
////        assertEquals(hello, new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));
//    }
//
//
//    @Test
//    public void getUser_invalidId_badRequest() {
//        assertThrows(BadRequestException.class, () ->
//                target("users/10").request().get(User.class));
//    }
//
//
//    @Test
//    public void getUsers() {
//        final User hello = target("users/1").request().get(User.class);
//        assertEquals(hello, new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));
//    }


//    @Test
//    public void getUsers_i() {
//        assertThrows(BadRequestException.class, () ->
//                target("users/10").request().get(User.class));
//    }

}