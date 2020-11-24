package service.resources;

import io.jsonwebtoken.Claims;
import service.Controller;
import service.model.User;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthenticationFilter implements ContainerRequestFilter {



    /**
     - resourceInfo contains information about the requested operation (GET,
     PUT, POST …).
     - resourceInfo will be assigned/set automatically by the Jersey
     framework, you do not need to assign/set it.
     */
    @Context
    private ResourceInfo resourceInfo;
    User user = null;


    // requestContext contains information about the HTTP request message
    @Override
    public void filter(ContainerRequestContext requestContext) {
        // here you will perform AUTHENTICATION and AUTHORIZATION
        /* if you want to abort this HTTP request, you do this:

      Response response = Response.status(Response.Status.UNAUTHORIZED).build();
      requestContext.abortWith(response);

        */

        // AUTHENTICATION:
        // 1. extract username and password from requestContext
        // 2. validate username and password (e.g., database)
        // 3. if invalid user, abort requestContext with UNAUTHORIZED response

        // AUTHORIZATION:
        // 1. extract allowed roles for requested operation from resourceInfo
        // 2. check if the user has one of these roles
        // 3. if not, abort requestContext with FORBIDDEN response



        /* Get information about the service method which is being called. This information includes the annotated/permitted roles. */
        Method method = resourceInfo.getResourceMethod();

        // if access is allowed for all -> do not check anything further : access is approved for all
        // if you want all logged in users to be able to access this route (add this check at the end of the class)
        if (method.isAnnotationPresent(PermitAll.class)) {
            System.out.println("Permit all");
            return;
        }

        // if access is denied for all: deny access
        if (method.isAnnotationPresent(DenyAll.class)) {
            Response response = Response.status(Response.Status.FORBIDDEN).build();
            requestContext.abortWith(response);
            return;
        }


        final String AUTHORIZATION_PROPERTY = "Authorization"; // property were interested in in the header
        final String AUTHENTICATION_SCHEME = "Bearer"; // the scheme (value starts with Basic)

        //Get request headers
        final MultivaluedMap<String, String> headers = requestContext.getHeaders(); // headers list

        //Fetch authorization header
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY); // authorization header

        //If no authorization information present: abort with UNAUTHORIZED and stop
        if (authorization == null || authorization.isEmpty()) {
            System.out.println("Auth is empty or null");
            Response response = Response.status(Response.Status.UNAUTHORIZED).
                    entity("Missing username and/or password.").build();
            requestContext.abortWith(response); // inform the client it's aborted with the above content
            return;
        }

        System.out.println("Found auth");


        //Get encoded username and password
        final String encodedCredentials = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", ""); // remove scheme (Basic) and space

        if(encodedCredentials.length() == 0) {
            System.out.println("NO CREDENTIALS");
            Response response = Response.status(Response.Status.UNAUTHORIZED).
                    entity("Username and password are required").build();
            requestContext.abortWith(response);
            return;
        }


        // Validate token
        Claims token = null;
        try {
            token = Controller.decodeJWT(encodedCredentials);
        }
        catch (Exception exception){
            //Invalid signature/claims
            Response response = Response.status(Response.Status.UNAUTHORIZED).
                    entity("Token is invalid " + exception.getMessage()).build();
            requestContext.abortWith(response);
            return;
        }



        /* here you do
        1.	the AUTHENTICATION first (as explained in previous sections), and
        2.	if AUTHENTICATION succeeds, you do the authorization like this:
        */
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            // get allowed roles for this method
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

            /* isUserAllowed : implement this method to check if this user has any of the roles in the rolesSet
            if not isUserAllowed abort the requestContext with FORBIDDEN response*/
            if (!isUserAllowed(rolesSet, Boolean.parseBoolean(token.get("admin").toString()))) {
                Response response = Response.status(Response.Status.FORBIDDEN).build();
                requestContext.abortWith(response);
                return;
            }
        }


    }


    // Check user's role
    private boolean isUserAllowed(Set<String> rolesSet, boolean isAdmin) {
        // iterate over the roles and compare it with the user's role
        // the role of the user can be retrieved as follows:
        // 1. When validating, we save the user object
        // 2. Select the user object here and check the role

        if((rolesSet.contains("admin") && isAdmin) || (rolesSet.contains("user") && !isAdmin)) {
            System.out.println("User allowed");
            return true;
        }
        System.out.println("User not allowed");

        return false;
    }
}