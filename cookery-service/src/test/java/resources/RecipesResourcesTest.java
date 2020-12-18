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
import service.model.Ingredient;
import service.model.Recipe;
import service.model.Role;
import service.model.User;
import service.repository.JDBCRepository;
import service.resources.RecipesResources;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.DriverManager;
import java.util.Arrays;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecipesResourcesTest extends JerseyTest {
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
        return new ResourceConfig(RecipesResources.class);
    }



//    INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 1', 'recipe 1 desc', 'recipe 1 image', 1);
//    INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 2', 'recipe 2 desc', 'recipe 2 image', 1);
//    INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 3', 'recipe 3 desc', 'recipe 3 image', 2);
//    INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 4', 'recipe 4 desc', 'recipe 4 image', 3);
//    @Test
//    public void getRecipesByIngredient() {
//        List<Recipe> expectedRecipes =  Arrays.asList(
//                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
//                        Arrays.asList(
//                                new Ingredient(1, "onion", 2),
//                                new Ingredient(2, "garlic", 1)
//                        ), 1),
//                new Recipe(2, "recipe 4", "recipe 4 image", "recipe 4 desc",
//                        Arrays.asList(
//                                new Ingredient(4, "onion", 5) //('onion', 5, 4)
//                        ),1)
//        );
////         final List<User> actualUsers =  target("users").request().get(new GenericType<List<User>>(){});
//        Response response = target("recipes?ingredient=onion").request().get();
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//    }
//
//
//    @Test
//    public void getRecipes() {
//        List<Recipe> expectedRecipes =  Arrays.asList(
//                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
//                        Arrays.asList(
//                                new Ingredient(1, "onion", 2),
//                                new Ingredient(2, "garlic", 1)
//                        ), 1),
//                new Recipe(2, "recipe 4", "recipe 4 image", "recipe 4 desc",
//                        Arrays.asList(
//                                new Ingredient(4, "onion", 5) //('onion', 5, 4)
//                        ),1)
//        );
////         final List<User> actualUsers =  target("users").request().get(new GenericType<List<User>>(){});
//        Response response = target("recipes").request().get();
//
//        assertEquals(expectedRecipes, response.readEntity(new GenericType<List<Recipe>>() {}));
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//    }

//    @GET //GET at http://localhost:XXXX/recipes?ingredient=onion OR http://localhost:XXXX/recipes
//    @PermitAll
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getRecipeByIngredient(@DefaultValue("all") @QueryParam("ingredient") String ingredient, @HeaderParam("Authorization") String auth){
//        List<RecipeDTO> recipes = new ArrayList<>();
//
//        int userId = -1;
//
//        if(!auth.equals("Bearer " + null)) { // auth exists
//            userId = authController.getIdInToken(auth);
//        }
//
//        if(ingredient.equals("all")) {
//            recipes = recipesController.getRecipesDTO(userId);
//        }
//
//
//        GenericEntity<List<RecipeDTO>> entity = new GenericEntity<List<RecipeDTO>>(recipes){ };
//        return Response.ok(entity).build();
//    }

    @Test
    public void getRecipe() {
        Recipe expectedRecipe = new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                        Arrays.asList(
                                new Ingredient(1, "onion", 2),
                                new Ingredient(2, "garlic", 1)
                        ), 1);

        Response response = target("recipes/1").request().get();

        assertEquals(expectedRecipe, response.readEntity(Recipe.class));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getRecipe_invalidId_badRequest() {
        Response response = target("recipes/6").request().get();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void addRecipe() {
        Recipe newRecipe = new Recipe(5, "recipe 5", "recipe 5 image", "recipe 5 desc",
                Arrays.asList(
                        new Ingredient(5, "onion", 4),
                        new Ingredient(6, "courgette", 2)
                ), 3);
        Entity<Recipe> recipeEntity = Entity.entity(newRecipe, MediaType.APPLICATION_JSON);

        Response response = target("recipes").request().post(recipeEntity);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }


//    @Test
//    public void updateRecipe() {
//        String token = AuthController.generateAuthToken(new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));
//
//        Recipe updatedRecipe = new Recipe(1, "recipe 1", "recipe 1 image new", "recipe 1 desc",
//                Arrays.asList(
//                        new Ingredient(1, "onion", 2),
//                        new Ingredient(5, "olives", 3)
//                ), 1);
//
//        Entity<Recipe> recipeEntity = Entity.entity(updatedRecipe, MediaType.APPLICATION_JSON);
//
//        Response response = target("recipes/1")
//                .request()
//                .header("Authorization", "Bearer " + token)
//                .put(recipeEntity);
//
//        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
//    }

    @Test
    public void updateRecipe_notOwner_forbidden() {
        String token = AuthController.generateAuthToken(new User(4, "Raneem", "raneem@gmail.com", "1234", Role.user));

        Recipe updatedRecipe = new Recipe(1, "recipe 1", "recipe 1 image new", "recipe 1 desc",
                Arrays.asList(
                        new Ingredient(1, "onion", 2),
                        new Ingredient(5, "olives", 3)
                ), 1);

        Entity<Recipe> recipeEntity = Entity.entity(updatedRecipe, MediaType.APPLICATION_JSON);

        Response response = target("recipes/1")
                .request()
                .header("Authorization", "Bearer " + token)
                .put(recipeEntity);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


//    @Test
//    public void updateRecipe_invalidId_notFound() {
//        String token = AuthController.generateAuthToken(new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));
//
//        Recipe updatedRecipe = new Recipe(1, "recipe 1", "recipe 1 image new", "recipe 1 desc",
//                Arrays.asList(
//                        new Ingredient(1, "onion", 2),
//                        new Ingredient(5, "olives", 3)
//                ), 1);
//
//        Entity<Recipe> recipeEntity = Entity.entity(updatedRecipe, MediaType.APPLICATION_JSON);
//
//        Response response = target("recipes/5")
//                .request()
//                .header("Authorization", "Bearer " + token)
//                .put(recipeEntity);
//
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//    }

//
//    @PUT //PUT at http://localhost:XXXX/recipes/3
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("{id}")
//    @RolesAllowed({"user", "admin"})
//    public Response updateRecipe(@PathParam("id") int id, Recipe recipe, @HeaderParam("Authorization") String auth){
//        int userId = authController.getIdInToken(auth); // id in token
//        int ownerId = UsersController.getUserId(id); // id of owner of the recipe
//
//        if(userId != ownerId) {
//            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
//        }
//
//        boolean result;
//
//        result = recipesController.updateRecipe(id, recipe);
//
//        if(result) {
//            return Response.noContent().build();
//        }
//        else {
//            return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid recipe id").build();
//        }
//    }

    @Test
    public void deleteRecipe() {
        String token = AuthController.generateAuthToken(new User(1, "Rawan", "rawan@gmail.com", "1234", Role.admin));

        Response response = target("recipes/1")
                            .request()
                            .header("Authorization", "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

//    @Test
//    public void deleteRecipe_notOwner_forbidden() {
//        String token = AuthController.generateAuthToken(new User(4, "Raneem", "raneem@gmail.com", "1234", Role.user));
//
//        Response response = target("recipes/1")
//                .request()
//                .header("Authorization", "Bearer " + token)
//                .delete();
//
//        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
//    }
}
