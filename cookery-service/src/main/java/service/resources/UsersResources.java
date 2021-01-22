package service.resources;

import cyclops.control.Validated;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import service.controller.AuthController;
import service.controller.NotificationController;
import service.controller.RecipesController;
import service.controller.UsersController;
import service.model.DTO.*;
import service.model.Recipe;
import service.model.Role;
import service.model.User;
import service.validators.Error;
import service.validators.UserValidator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

//import java.io.*;
//import javax.ws.rs.*;


@Path("/users")
public class UsersResources {

    @Context
    private UriInfo uriInfo;

    private final UsersController usersController = new UsersController();
    private final RecipesController recipesController = new RecipesController();
    private final AuthController authController = new AuthController();
    private final NotificationController notificationController = new NotificationController();


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
    @RolesAllowed({"user", "admin"})
//    @PermitAll
    public Response getUser(@PathParam("id") int id){
        UserDTO user = usersController.getUser(id);

        if(user != null){
            return Response.ok(user).build(); // Status ok 200, return user
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid user id").build();
        }
    }


    @GET //GET at http://localhost:XXXX/users/1/profile
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/profile")
    @RolesAllowed({"user", "admin"})
    public Response getUserProfile(@PathParam("id") int id, @HeaderParam("Authorization") String auth){
        ProfileDTO profile = usersController.getProfile(id);
        if(profile != null){
            return Response.ok(profile).build(); // Status ok 200, return profile
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid user id").build();
        }
    }


    @POST //POST at http://localhost:XXXX/users
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response addUser(User user){
        // Validate data
        Validated<Error, String> name = UserValidator.validName(user);;
        Validated<Error, String> email = UserValidator.validEmail(user);
        Validated<Error, String> pass = UserValidator.validPassword(user);

        // if name is invalid , if email is invalid, if password is invalid -> incorrect data
        if(name.isInvalid() || email.isInvalid() || pass.isInvalid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
        }
        else {
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
    }


    @PUT //PUT at http://localhost:XXXX/users/3
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @RolesAllowed({"user", "admin"})
    public Response updateUser(@PathParam("id") int id, User user, @HeaderParam("Authorization") String auth){
        UserDTO userInToken = AuthController.getUser(auth); // user in token

        // If someone else is trying to change the data, and user trying to change the role
        if((user.getId() != userInToken.getId() && userInToken.getRole().equals(Role.valueOf("user")))) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
        }

        // Validate data
        Validated<Error, String> name = UserValidator.validName(user);;
        Validated<Error, String> email = UserValidator.validEmail(user);
        Validated<Error, String> pass = UserValidator.validPassword(user);

        // if name is not null and valid , if email not null and valid, if password is not null and invalid -> incorrect data
        if((user.getName() != null && name.isInvalid()) || (user.getEmail() != null && email.isInvalid()) || (user.getPassword() != null && pass.isInvalid())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
        }
        else {
            boolean result = usersController.updateUser(id, user);

            if (result) { // Successful
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid user id").build(); // Status not found, return error message
            }
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
        List<Recipe> recipes = recipesController.getRecipes(id);

        System.out.println("Recipes for user " + id);
        System.out.println("Recipes size " + recipes.size());

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


    @PUT //PUT at http://localhost:XXXX/users/1/image
    @Path("{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PermitAll
    @RolesAllowed({"user", "admin"}) // logged in users
    public Response uploadImage(@FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileMetaData,
                                @PathParam("id") int id,
                                @HeaderParam("Authorization") String auth) throws IOException {
        int userId = AuthController.getIdInToken(auth); // id in token

        UsersController usersController = new UsersController();

        String UPLOAD_PATH = "cookery-service/src/images/";

        File file = getFileName(new File(UPLOAD_PATH + fileMetaData.getFileName()));

        if(Files.probeContentType(file.toPath()).contains("image")) {
            int read = 0;
            byte[] bytes = new byte[1024];
            String completePath = file.toString(); //+id;
            OutputStream out = null;
            try {
                out = new FileOutputStream(completePath);

                while ((read = fileInputStream.read(bytes)) != -1)
                {
                    out.write(bytes, 0, read);
                }

                boolean result = usersController.uploadImage(userId, file.getName());

                if(result){
                    out.flush();
                    out.close();
                }
                else {
                    return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid user id").build(); // Status not found, return error message
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                if(fileInputStream != null) {
                    fileInputStream.close();
                }
            }

            return Response.noContent().build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Image is invalid").build();
        }
    }


    @GET //GET at http://localhost:XXXX/users
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    @Path("{id}/followers")
    public Response getFollowers(@HeaderParam("Authorization") String auth){
        int userId = AuthController.getIdInToken(auth); // id in token

        List<UserFollowDTO> users = usersController.getFollowers(userId);

        GenericEntity<List<UserFollowDTO>> entity = new GenericEntity<List<UserFollowDTO>>(users){ };
        return Response.ok(entity).build();
    }


    @GET //GET at http://localhost:XXXX/users
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    @Path("{id}/followees")
    public Response getFollowees(@HeaderParam("Authorization") String auth){
        int userId = AuthController.getIdInToken(auth); // id in token

        List<UserFollowDTO> users = usersController.getFollowees(userId);

        GenericEntity<List<UserFollowDTO>> entity = new GenericEntity<List<UserFollowDTO>>(users){ };
        return Response.ok(entity).build();
    }


    @GET //GET at http://localhost:XXXX/users/1/notifications
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    @Path("{id}/notifications")
    public Response getNotifications(@HeaderParam("Authorization") String auth) {
        int userId = AuthController.getIdInToken(auth); // id in token

        NotificationDTO notification = notificationController.getNotifications(userId);

        return Response.ok(notification).build();
    }


    @PUT //GET at http://localhost:XXXX/users/1/notifications
    @RolesAllowed({"user", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/notifications/seen")
    public Response markNotificationsAsSeen(@HeaderParam("Authorization") String auth) {
        int userId = AuthController.getIdInToken(auth); // id in token

        boolean result = notificationController.markAsSeen(userId);

        return Response.noContent().build();
    }


    private File getFileName(File file) {
        if (file.exists()){
            String newFileName = file.getName();
            String simpleName = file.getName().substring(0,newFileName.indexOf("."));
            String strDigit="";

            try {
                simpleName = (Integer.parseInt(simpleName)+1+"");
                File newFile = new File(file.getParent()+"/"+simpleName+getExtension(file.getName()));
                return getFileName(newFile);
            }
            catch (Exception e){

            }

            for (int i=simpleName.length()-1;i>=0;i--){
                if (!Character.isDigit(simpleName.charAt(i))){
                    strDigit = simpleName.substring(i+1);
                    simpleName = simpleName.substring(0,i+1);
                    break;
                }
            }

            if (strDigit.length()>0){
                simpleName = simpleName+(Integer.parseInt(strDigit)+1);
            } else {
                simpleName+="1";
            }

            File newFile = new File(file.getParent() + "/" + simpleName+getExtension(file.getName()));
            return getFileName(newFile);
        }
        return file;
    }

    private String getExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

}