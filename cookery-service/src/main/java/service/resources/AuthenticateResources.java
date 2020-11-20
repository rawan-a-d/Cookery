package service.resources;

import service.Controller;
import service.model.User;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.StringTokenizer;

@Path("authenticate")
public class AuthenticateResources {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response authenticate(String credentials) {
        StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
        String email = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        Controller controller = new Controller();

        User user = controller.authenticate(email, password);

        if(user != null) {

            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).
                    entity("Invalid username and/or password.").build();
        }
    }


    // getting route
    // api/orders GET
    // check the headers if it contains Authorization which has a valid token
    // we respond with 200 and the orders list
    // otherwise,  response 401 unauthorized

}
