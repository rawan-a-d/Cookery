package service.resources;

import service.Controller;
import service.model.Favourite;
import service.model.Recipe;
import service.model.User;
import service.repository.DataStore;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/users")
public class UsersResources {

    @Context
    private UriInfo uriInfo;

    private final DataStore dataStore = DataStore.getInstance();


    @GET //GET at http://localhost:XXXX/users
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public Response getAllUsers(){
        Controller controller = new Controller();

        List<User> users = controller.getUsers();

        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users){ };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:XXXX/users/1
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @RolesAllowed({"user", "admin"})
    public Response getUser(@PathParam("id") int id){
        Controller controller = new Controller();

        User user = controller.getUser(id);

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
        Controller controller = new Controller();
        boolean result = controller.createUser(user);

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
        Controller controller = new Controller();
        boolean result = controller.updateUser(id, user);

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
        Controller controller = new Controller();
        controller.deleteRecipe(id);

        return Response.noContent().build();
    }


    @GET //GET at http://localhost:XXXX/users/2/recipes
    @Path("{id}/recipes")
    public Response getUserRecipes(@PathParam("id") int id, @HeaderParam("Authorization") String auth){ // GET RECIPES BASED ON URL OR Authorization Header????????????????
        Controller controller = new Controller();
        int userId = controller.getIdInToken(auth);

        List<Recipe> recipes = controller.getRecipes(userId);

        GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
        return Response.ok(entity).build();
    }


    @POST
    @Path("{id}/favourites")
    public Response addFavourite(@PathParam("id") int id, Favourite favourite) {
        Controller controller = new Controller();
        boolean result = controller.addFavourite(favourite);

        if(result){ // Successful
            String url = uriInfo.getAbsolutePath() + "/" + favourite.getRecipeId();// url of the created user
            URI uri = URI.create(url);
            return Response.created(uri).build();
        }
        else {
            String entity = "User with id " + favourite.getRecipeId() + " already marked recipe with id " + favourite.getRecipeId() + " as favourite";
            return Response.status(Response.Status.CONFLICT).entity(entity).build(); // status conflict, return previous reply
        }
    }


    @DELETE
    @Path("{id}/favourites/{favouriteId}")
    public Response deleteFavourite(@PathParam("id") int id, @PathParam("favouriteId") int favouriteId) {
        Controller controller = new Controller();
        controller.removeFavourite(favouriteId);

        return Response.noContent().build();
    }

    @GET //GET at http://localhost:XXXX/users
    @Path("{id}/favourites")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFavourites(@HeaderParam("Authorization") String auth){
        Controller controller = new Controller();

//        System.out.println("FAOVU");

        int userId = controller.getIdInToken(auth); // id in token

        List<Recipe> recipes = controller.getFavourites(userId);

        GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
        return Response.ok(entity).build();
    }






}
