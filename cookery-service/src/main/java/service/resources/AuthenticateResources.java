package service.resources;

import com.auth0.jwt.exceptions.JWTCreationException;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Base64;

@Path("authenticate")
public class AuthenticateResources {

    // api/authenticate
    // post request
    // get the email and password
    // check if they are correct
    // Send a response back with JSON Token
    // otherwise, response 400 bad request with nothing in it
    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)

//    @Path("authenticate")
    public Response authenticate(@HeaderParam("Authorization") String auth ) {
        try {

            System.out.println("AUTH");
            System.out.println(auth);

            String credentials2 = new String(Base64.getDecoder().decode(auth.getBytes()));
            System.out.println("DECODED");
            System.out.println(credentials2);

            return Response.ok().build();
        }
        catch (JWTCreationException exception) {
            return Response.status(401).build();
        }


    }


    // getting route
    // api/orders GET
    // check the headers if it contains Authorization which has a valid token
    // we respond with 200 and the orders list
    // otherwise,  response 401 unauthorized

}
