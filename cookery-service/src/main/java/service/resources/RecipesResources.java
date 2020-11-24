package service.resources;

import service.Controller;
import service.model.Recipe;
import service.repository.DataStore;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/recipes")
public class RecipesResources {

	@Context
	private UriInfo uriInfo;

	private final DataStore dataStore = DataStore.getInstance();


	@GET //GET at http://localhost:XXXX/recipes?ingredient=onion OR http://localhost:XXXX/recipes
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipeByIngredient(@DefaultValue("all") @QueryParam("ingredient") String ingredient){
		List<Recipe> recipes;
		Controller controller = new Controller();
		if(ingredient.equals("all")) {
			recipes = controller.getRecipes();
		}
		else {
			recipes = controller.getRecipes(ingredient);
		}

		GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
		return Response.ok(entity).build();

	}

	@GET //GET at http://localhost:XXXX/recipes/1
	@Path("{id}")
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipe(@PathParam("id") int id) {
		Controller controller = new Controller();

		Recipe recipe = controller.getRecipe(id);
		if(recipe != null){
			return Response.ok(recipe).build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid recipe id").build();
		}
	}


	@POST //POST at http://localhost:XXXX/recipes
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"user", "admin"})
	public Response addRecipe(Recipe recipe){
		Controller controller = new Controller();
		controller.createRecipe(recipe);

		String url = uriInfo.getAbsolutePath() + "/" + recipe.getId();
		URI uri = URI.create(url);
		return Response.created(uri).build();
	}

	@PUT //PUT at http://localhost:XXXX/recipes/3
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	@RolesAllowed({"user", "admin"})
	public Response updateRecipe(@PathParam("id") int id, Recipe recipe, @HeaderParam("Authorization") String auth){
		Controller controller = new Controller();

		int userId = controller.getIdInToken(auth); // id in token
		int ownerId = controller.getUserId(id); // id of owner of the recipe

		if(userId != ownerId) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
		}

		boolean result;

		result = controller.updateRecipe(id, recipe);

		if(result) {
			return Response.noContent().build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid recipe id").build();
		}
	}

	@DELETE //DELETE at http://localhost:XXXX/recipes/3
	@Path("{id}")
	@RolesAllowed({"user", "admin"})
	public Response deleteRecipe(@PathParam("id") int id, @HeaderParam("Authorization") String auth){
		Controller controller = new Controller();

		int userId = controller.getIdInToken(auth); // id in token
		int ownerId = controller.getUserId(id); // id of owner of the recipe

		if(userId != ownerId) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
		}

		controller.deleteRecipe(id);

		return Response.noContent().build();
	}
}