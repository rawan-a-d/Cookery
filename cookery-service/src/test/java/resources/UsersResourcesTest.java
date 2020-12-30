package resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.AuthController;
import service.model.DTO.RecipeDTO;
import service.model.DTO.UserDTO;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.Role;
import service.model.User;
import service.repository.JDBCRepository;
import service.resources.UsersResources;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UsersResourcesTest extends JerseyTest {
    @Mock
    JDBCRepository jdbcRepository;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        Mockito.lenient().when(jdbcRepository.getDatabaseConnection()).thenReturn(
                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
        );

        super.setUp();

        Class.forName ("org.h2.Driver");
        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:data.sql", UTF8, false);
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:shutdown.sql", UTF8, false);
    }


    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0"); // runs on available port
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(UsersResources.class);
//                .register("app.properties");
    }



    @Test
    public void getUsers() {
        List<UserDTO> expectedUsers = Arrays.asList(
                new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin),
                new UserDTO(2, "Anas", "anas@gmail.com", Role.user),
                new UserDTO(3, "Omar", "omar@gmail.com", Role.admin),
                new UserDTO(4, "Raneem", "raneem@gmail.com", Role.user)
        );

        final List<UserDTO> actualUsers =  target("users").request().get(new GenericType<List<UserDTO>>(){});
        assertEquals(expectedUsers, actualUsers);
    }


    @Test
    public void getUser() {
        UserDTO expectedUser = new UserDTO(2, "Anas", "anas@gmail.com", Role.user);

        Response response = target("users/2").request().get();

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedUser, response.readEntity(UserDTO.class));
    }


    @Test
    public void getUser_invalidId_badRequest() {
        assertThrows(BadRequestException.class, () ->
                target("users/10").request().get(User.class));
    }


    @Test
    public void addUser() {
        User newUser = new User("Denys", "denys@gmail.com", "Qw1234576@");

        Entity<User> userEntity = Entity.entity(newUser, MediaType.APPLICATION_JSON);
        Response response = target("users").request().post(userEntity);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }


    @Test
    public void deleteUser() {
        Response response = target("users/4").request().delete();

        assertEquals(204, response.getStatus());
    }


    @Test
    public void updateUser(){
        User updatedUser = new User(1, "Rawan", "rawan.ad@gmail.com", "Qw1234576@", Role.admin);

        Entity<User> userEntity = Entity.entity(updatedUser, MediaType.APPLICATION_JSON);
        Response response = target("users/1").request().put(userEntity);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }


    @Test
    public void updateUser_invalidId(){
        User updatedUser = new User(5, "denys", "denys@gmail.com", "Qw1234576@", Role.user);

        Entity<User> userEntity = Entity.entity(updatedUser, MediaType.APPLICATION_JSON);
        Response response = target("users/5").request().put(userEntity);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }


    @Test
    public void getUserRecipes() {
        String token = AuthController.generateAuthToken(new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin));
        List<Recipe> expectedRecipes =  Arrays.asList(
                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                        Arrays.asList(
                                new Ingredient(1, "onion", 2),
                                new Ingredient(2, "garlic", 1)
                        ), 1),
                new Recipe(2, "recipe 2", "recipe 2 image", "recipe 2 desc",
                        Arrays.asList(
                                new Ingredient(3, "tomato", 2)
                        ),1)
        );


        Response response = target("users/1/recipes")
                .request()
                .header("Authorization", "Bearer " + token)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedRecipes, response.readEntity(new GenericType<List<Recipe>>() {}));
    }


//    @Test
//    public void getUserRecipes_noRecipes_returnsNull() {
//        String token = AuthController.generateAuthToken(new User(4, "Raneem", "raneem@gmail.com", "1234", Role.user));
//
//        Response response = target("users/4/recipes").request().header("Authorization", "Bearer " + token).get();
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//        assertEquals(null, response.readEntity(new GenericType<List<Recipe>>() {}));
//    }


    @Test
    public void addFavourite() {
        String token = AuthController.generateAuthToken(new UserDTO(4, "Raneem", "raneem@gmail.com", Role.user));
        RecipeDTO recipe = new RecipeDTO(4, new UserDTO(4, "Raneem"));

        Entity<RecipeDTO> recipeEntity = Entity.entity(recipe, MediaType.APPLICATION_JSON);
        Response response = target("users/4/favourites")
                            .request()
                            .header("Authorization", "Bearer " + token)
                            .post(recipeEntity);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }


    @Test
    public void addFavourite_alreadyFavourite_conflict() {
        String token = AuthController.generateAuthToken(new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin));
        RecipeDTO recipe = new RecipeDTO(6, new UserDTO(1, "Rawan"));

        Entity<RecipeDTO> recipeEntity = Entity.entity(recipe, MediaType.APPLICATION_JSON);
        Response response = target("users/1/favourites")
                            .request()
                            .header("Authorization", "Bearer " + token)
                            .post(recipeEntity);

        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }


    @Test
    public void deleteFavourite() {
        Response response = target("users/1/favourites/2").request().delete();

        assertEquals(204, response.getStatus());
    }


    @Test
    public void getFavouritesDTO() {
        String token = AuthController.generateAuthToken(new UserDTO(1, "Rawan", "rawan@gmail.com", Role.admin));
        List<RecipeDTO> expectedRecipes =  Arrays.asList(
                new RecipeDTO(2, "recipe 2", "recipe 2 image",
                        new UserDTO(1, "Rawan"), 1, true),
                new RecipeDTO(3, "recipe 3", "recipe 3 image",
                        new UserDTO(2, "Anas"), 2, true)
        );

        Response response = target("users/1/favourites")
                                        .request()
                                        .header("Authorization", "Bearer " + token)
                                        .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedRecipes, response.readEntity(new GenericType<List<RecipeDTO>>() {}));
    }
}