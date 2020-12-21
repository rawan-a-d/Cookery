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

    @GET //GET at http://localhost:XXXX/statistics
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavouritesPerUser(@QueryParam("type") String type){
//        List<ChartDataDTO> chartData = null;
        ChartDataDTO chartData = null;

        if(type.equals("favourites-recipes-per-user")) {
            chartData = statisticsController.getFavouritesPerUser();
        }
        else if(type.equals("recipes-per-month")) {
            chartData = statisticsController.getPostedRecipesPerMonth();
        }
        else {
            System.out.println("No type");
        }

//        GenericEntity<List<ChartDataDTO>> entity = new GenericEntity<List<ChartDataDTO>>(chartData){ };
        return Response.ok(chartData).build(); // Status ok 200, return user

//        return Response.ok(entity).build();
    }
}
