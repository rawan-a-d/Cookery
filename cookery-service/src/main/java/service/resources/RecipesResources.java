package service.resources;

import service.Controller;
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


	@GET //GET at http://localhost:XXXX/recipes?ingredient=onion OR http://localhost:XXXX/recipes
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipeByIngredient(@DefaultValue("all") @QueryParam("ingredient") String ingredient){
		List<Recipe> recipes;
		Controller controller = new Controller();
		if(ingredient.equals("all")) {
//			recipes = dataStore.getRecipes();

			recipes = controller.getRecipes();
		}
		else {
//			recipes = dataStore.getRecipesBy(ingredient);

			recipes = controller.getRecipes(ingredient);
		}

		GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes){ };
		return Response.ok(entity).build();

	}

	@GET //GET at http://localhost:XXXX/recipes/1
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getRecipe(@PathParam("id") int id) {
//		Recipe recipe = dataStore.getRecipe(id);
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
	public Response addRecipe(Recipe recipe){

//		boolean result = dataStore.addRecipe(recipe);
		Controller controller = new Controller();
		controller.createRecipe(recipe);

		String url = uriInfo.getAbsolutePath() + "/" + recipe.getId();
		URI uri = URI.create(url);
		return Response.created(uri).build();
	}

	@PUT //PUT at http://localhost:XXXX/recipes/3
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateRecipe(@PathParam("id") int id, Recipe recipe){
//		boolean result = dataStore.updateRecipe(id, recipe);

		Controller controller = new Controller();


		boolean result = controller.updateRecipe(id, recipe);

		System.out.println("recipe received: "+ recipe);
		System.out.println("REACHED");

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
//		dataStore.deleteRecipe(id);
		Controller controller = new Controller();
		controller.deleteRecipe(id);

		return Response.noContent().build();
	}
}