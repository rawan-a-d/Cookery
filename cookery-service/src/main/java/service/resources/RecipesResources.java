package service.resources;

import service.model.Recipe;
import service.repository.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/recipes")
public class RecipesResources {

	@Context
	private UriInfo uriInfo;

	private final DataStore dataStore = DataStore.getInstance();

	@GET //GET at http://localhost:XXXX/recipes
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRecipes(){
		List<Recipe> recipes = dataStore.getRecipes();

		GenericEntity<List<Recipe>> entity = new GenericEntity<>(recipes){ };
		return Response.ok(entity).build();
	}

	@GET //GET at http://localhost:XXXX/recipes/1
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getRecipe(@PathParam("id") int id) {
		Recipe recipe = dataStore.getRecipe(id);

		if(recipe != null){
			return Response.ok(recipe).build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid recipe id").build();
		}
	}


	@POST //POST at http://localhost:XXXX/recipes
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRecipe(Recipe recipe){
		boolean result = dataStore.addRecipe(recipe);

		String url = uriInfo.getAbsolutePath() + "/" + recipe.getId();
		URI uri = URI.create(url);
		return Response.created(uri).build();
	}

	@PUT //PUT at http://localhost:XXXX/recipes/3
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateRecipe(@PathParam("id") int id, Recipe recipe){
		boolean result = dataStore.updateRecipe(id, recipe);

		if(result) {
			return Response.noContent().build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid recipe id").build();
		}
	}

	@DELETE //DELETE at http://localhost:XXXX/recipes/3
	@Path("{id}")
	public Response deleteRecipe(@PathParam("id") int id){
		dataStore.deleteRecipe(id);
		return Response.noContent().build();
	}
}