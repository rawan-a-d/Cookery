package service.resources;

import service.controller.StatisticsController;
import service.model.DTO.ChartDataDTO;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/statistics")
@RolesAllowed({"admin"})
public class StatisticsResources {
    @Context
    private UriInfo uriInfo;

    private final StatisticsController statisticsController = new StatisticsController();

    @GET //GET at http://localhost:XXXX/statistics?type="favourites-recipes-per-user"
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatistic(@QueryParam("type") String type){
        ChartDataDTO chartData = null;

        if(type.equals("favourites-recipes-per-user")) {
            chartData = statisticsController.getFavouritesPerUser();
        }
        else if(type.equals("recipes-per-month")) {
            chartData = statisticsController.getPostedRecipesPerMonth();
        }
        else if(type.equals("top-followed-users")) {
            chartData = statisticsController.getTopFollowedUsers();
        }
        else {
            System.out.println("No type");
        }

        return Response.ok(chartData).build(); // Status ok 200, return user
    }
}
