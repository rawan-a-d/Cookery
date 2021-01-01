package service.resources;

import service.controller.AuthController;
import service.controller.RecipesController;
import service.controller.UsersController;
import service.model.DTO.RecipeDTO;
import service.model.DTO.UserDTO;
import service.model.Recipe;
import service.model.Role;
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
    AuthController authController = new AuthController();


    @GET //GET at http://localhost:XXXX/users
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response getAllUsers(){
        List<UserDTO> users = usersController.getUsers();

        GenericEntity<List<UserDTO>> entity = new GenericEntity<List<UserDTO>>(users){ };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:XXXX/users/1
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
//    @RolesAllowed({"user", "admin"})
    @PermitAll
    public Response getUser(@PathParam("id") int id){
        UserDTO user = usersController.getUser(id);

        if(user != null){
            return Response.ok(user).build(); // Status ok 200, return user
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid user id").build();
        }
    }


    @POST //POST at http://localhost:XXXX/users
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addUser(User user){
        UserDTO userDTO = usersController.createUser(user);

        if(userDTO != null){ // Successful
            String token = AuthController.generateAuthToken(userDTO);

            return Response.ok(token).build();
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
    public Response updateUser(@PathParam("id") int id, User user, @HeaderParam("Authorization") String auth){
        UserDTO userInToken = AuthController.getUser(auth); // user in token

        if(user.getId() != userInToken.getId()) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
        }

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
    public Response deleteUser(@PathParam("id") int id, @HeaderParam("Authorization") String auth){
        UserDTO userInToken = AuthController.getUser(auth); // user in token

        if(userInToken.getRole() != Role.admin) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
        }
        usersController.deleteUser(id);

        return Response.noContent().build();
    }


    @GET //GET at http://localhost:XXXX/users/2/recipes
    @Path("{id}/recipes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"}) // get recipes of logged in users
    public Response getUserRecipes(@PathParam("id") int id, @HeaderParam("Authorization") String auth){
        int userId = authController.getIdInToken(auth);

        List<Recipe> recipes = recipesController.getRecipes(userId);

        GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
        return Response.ok(entity).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/favourites")
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response addFavourite(@PathParam("id") int id, RecipeDTO favourite, @HeaderParam("Authorization") String auth) {
        int userId = authController.getIdInToken(auth);

        boolean result = recipesController.addFavourite(userId, favourite);

        if(result){ // Successful
            String url = uriInfo.getAbsolutePath() + "/" + favourite.getId();// url of the created favourite
            URI uri = URI.create(url);
            return Response.created(uri).build();
        }
        else {
            String entity = "User with id " + favourite.getId() + " already marked recipe with id " + favourite.getId() + " as favourite";
            return Response.status(Response.Status.CONFLICT).entity(entity).build(); // status conflict, return previous reply
        }
    }


    //****************
    @DELETE
    @Path("{id}/favourites/{favouriteId}")
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response deleteFavourite(@PathParam("id") int id, @PathParam("favouriteId") int favouriteId, @HeaderParam("Authorization") String auth) {
        int userId = authController.getIdInToken(auth); // id in token

        recipesController.deleteFavourite(userId, favouriteId);

        return Response.noContent().build();
    }


    @GET //GET at http://localhost:XXXX/users/1/favourites
    @Path("{id}/favourites")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response getAllFavourites(@HeaderParam("Authorization") String auth){
        int userId = authController.getIdInToken(auth); // id in token

        List<RecipeDTO> recipes = recipesController.getFavouritesDTO(userId);

        GenericEntity<List<RecipeDTO>> entity = new GenericEntity<List<RecipeDTO>>(recipes){ };
        return Response.ok(entity).build();
    }


    @POST //GET at http://localhost:XXXX/users/1/followers
    @Path("{id}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response follow(@HeaderParam("Authorization") String auth, UserDTO followee){
        int userId = authController.getIdInToken(auth); // id in token

        int followId = usersController.follow(userId, followee);

        if(followId > 0){ // Successful
            String url = uriInfo.getAbsolutePath() + "/" + followId;// url of the created follow
            URI uri = URI.create(url);
            return Response.created(uri).build();
        }
        else {
            String entity = "User with id " + userId + " already followed user with id " + followee.getId();
            return Response.status(Response.Status.CONFLICT).entity(entity).build(); // status conflict, return previous reply
        }
    }

    @DELETE
    @Path("{id}/followers/{followId}")
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response unFollow(@HeaderParam("Authorization") String auth, @PathParam("id") int id, @PathParam("followId") int followId) {
        int userId = authController.getIdInToken(auth); // id in token

        usersController.unFollow(userId, followId);

        return Response.noContent().build();
    }
}