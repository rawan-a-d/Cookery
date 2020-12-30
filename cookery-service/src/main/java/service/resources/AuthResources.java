package service.resources;

import service.controller.AuthController;
import service.controller.UsersController;
import service.model.Credentials;
import service.model.DTO.UserDTO;
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
        System.out.println(credentials);
        UserDTO user = authController.authenticate(credentials.getEmail(), credentials.getPassword());

        if(user != null) {
            String token = AuthController.generateAuthToken(user);

            System.out.println("Token " + token);

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
        System.out.println("USER "+ user);
//        ValidationUtils.<User>validate(user);

        UserDTO userDTO = usersController.createUser(user);

        if(userDTO != null) {
            String token = AuthController.generateAuthToken(userDTO);

            return Response.ok(token).build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).
                    entity("Invalid username and/or password.").build();
        }
    }

}
