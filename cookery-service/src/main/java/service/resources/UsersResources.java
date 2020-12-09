package service.resources;

import service.controller.AuthController;
import service.controller.RecipesController;
import service.controller.UsersController;
import service.model.DTO.RecipeDTO;
import service.model.Recipe;
import service.model.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/users")
public class UsersResources {

    @Context
    private UriInfo uriInfo;

    private final UsersController usersController = new UsersController();
    private final RecipesController recipesController = new RecipesController();
    private final AuthController authController = new AuthController();


    @GET //GET at http://localhost:XXXX/users
    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({"admin"})
    @PermitAll
    public Response getAllUsers(){
        List<User> users = usersController.getUsers();

        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users){ };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:XXXX/users/1
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @RolesAllowed({"user", "admin"})
    public Response getUser(@PathParam("id") int id){
        User user = usersController.getUser(id);

        if(user != null){
            return Response.ok(user).build(); // Status ok 200, return user
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid user id").build();
        }
    }


    @POST //POST at http://localhost:XXXX/users
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public Response addUser(User user){
        boolean result = usersController.createUser(user);

        if(result){ // Successful
            String url = uriInfo.getAbsolutePath() + "/" + user.getId();// url of the created user
            URI uri = URI.create(url);
            return Response.created(uri).build();
        }
        else {
            String entity = "User with id " + user.getId() + " already exists";
            return Response.status(Response.Status.CONFLICT).entity(entity).build(); // status conflict, return previous reply
        }
    }

    @PUT //PUT at http://localhost:XXXX/users/3
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @RolesAllowed({"user", "admin"})
    public Response updateUser(@PathParam("id") int id, User user){
        boolean result = usersController.updateUser(id, user);

        if(result) { // Successful
            return Response.noContent().build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid user id").build(); // Status not found, return error message
        }
    }

    @DELETE //DELETE at http://localhost:XXXX/users/3
    @Path("{id}")
    @RolesAllowed({"admin"})
    public Response deleteUser(@PathParam("id") int id){
        usersController.deleteUser(id);

        return Response.noContent().build();
    }


    @GET //GET at http://localhost:XXXX/users/2/recipes
    @Path("{id}/recipes")
    @PermitAll
    public Response getUserRecipes(@PathParam("id") int id, @HeaderParam("Authorization") String auth){ // GET RECIPES BASED ON URL OR Authorization Header????????????????
        int userId = authController.getIdInToken(auth);

        List<Recipe> recipes = recipesController.getRecipes(userId);

        GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
        return Response.ok(entity).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/favourites")
    public Response addFavourite(@PathParam("id") int id, RecipeDTO favourite, @HeaderParam("Authorization") String auth) {
        int userId = authController.getIdInToken(auth);

        boolean result = recipesController.addFavourite(userId, favourite);

        if(result){ // Successful
            String url = uriInfo.getAbsolutePath() + "/" + favourite.getId();// url of the created user
            URI uri = URI.create(url);
            return Response.created(uri).build();
        }
        else {
            String entity = "User with id " + favourite.getId() + " already marked recipe with id " + favourite.getId() + " as favourite";
            return Response.status(Response.Status.CONFLICT).entity(entity).build(); // status conflict, return previous reply
        }
    }


    @DELETE
    @Path("{id}/favourites/{favouriteId}")
    public Response deleteFavourite(@PathParam("id") int id, @PathParam("favouriteId") int favouriteId) {
        recipesController.removeFavourite(favouriteId);

        return Response.noContent().build();
    }

    @GET //GET at http://localhost:XXXX/users
    @Path("{id}/favourites")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public Response getAllFavourites(@HeaderParam("Authorization") String auth){
        int userId = authController.getIdInToken(auth); // id in token

        List<RecipeDTO> recipes = recipesController.getFavouritesDTO(userId);

        GenericEntity<List<RecipeDTO>> entity = new GenericEntity<List<RecipeDTO>>(recipes){ };
        return Response.ok(entity).build();
    }






}
