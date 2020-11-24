package service.resources;

import service.Controller;
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
public class AuthenticateResources {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(Credentials credentials) {
        Controller controller = new Controller();

        User user = controller.authenticate(credentials.getEmail(), credentials.getPassword());

        if(user != null) {

            String token = controller.generateAuthToken(user);

            System.out.println("Decoded JWT");
            System.out.println(controller.decodeJWT(token));

            return Response.ok(token).build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).
                    entity("Invalid username and/or password.").build();
        }
    }

}
