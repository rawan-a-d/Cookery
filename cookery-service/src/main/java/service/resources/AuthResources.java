package service.resources;

import service.controller.AuthController;
import service.controller.UsersController;
import service.model.Credentials;
import service.model.User;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authenticate")
public class AuthResources {

    private final UsersController usersController = new UsersController();
    private final AuthController authController = new AuthController();


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(Credentials credentials) {
        User user = authController.authenticate(credentials.getEmail(), credentials.getPassword());

        if(user != null) {
            String token = AuthController.generateAuthToken(user);

            return Response.ok(token).build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).
                    entity("Invalid username and/or password.").build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("register")
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(User user) {
        boolean result = usersController.createUser(user);

        if(result) {
            String token = AuthController.generateAuthToken(user);

            return Response.ok(token).build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).
                    entity("Invalid username and/or password.").build();
        }
    }

}
