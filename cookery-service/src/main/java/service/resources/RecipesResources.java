package service.resources;

import service.controller.AuthController;
import service.controller.RecipesController;
import service.controller.UsersController;
import service.model.DTO.RecipeDTO;
import service.model.Recipe;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/recipes")
public class RecipesResources {

	@Context
	private UriInfo uriInfo;

	private final UsersController usersController = new UsersController();
	private final RecipesController recipesController = new RecipesController();
	private final AuthController authController = new AuthController();

	@GET //GET at http://localhost:XXXX/recipes?ingredient=onion OR http://localhost:XXXX/recipes
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipeByIngredient(@DefaultValue("all") @QueryParam("ingredient") String ingredient, @HeaderParam("Authorization") String auth){
		List<RecipeDTO> recipes = new ArrayList<>();

		int userId = -1;
		System.out.println("AUTH " + auth);

		if(!auth.equals("Bearer " + null)) { // auth exists
			System.out.println("AUTH FOUND" + auth);
			userId = authController.getIdInToken(auth);

		}
		else {
			System.out.println("AUTH not FOUND " + auth);
		}

		if(ingredient.equals("all")) {
//			recipes = controller.getRecipes();
			recipes = usersController.getRecipesDTO(userId);
			System.out.println("RECIPES HOME ");
			System.out.println(recipes);
		}

		GenericEntity<List<RecipeDTO>> entity = new GenericEntity<>(recipes){ };
		return Response.ok(entity).build();

	}

	@GET //GET at http://localhost:XXXX/recipes/1
	@Path("{id}")
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipe(@PathParam("id") int id) {
		Recipe recipe = recipesController.getRecipe(id);
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

		System.out.println("New recipe");
		System.out.println(recipe);
		recipesController.createRecipe(recipe);

		String url = uriInfo.getAbsolutePath() + "/" + recipe.getId();
		URI uri = URI.create(url);
		return Response.created(uri).build();
	}

	@PUT //PUT at http://localhost:XXXX/recipes/3
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	@RolesAllowed({"user", "admin"})
	public Response updateRecipe(@PathParam("id") int id, Recipe recipe, @HeaderParam("Authorization") String auth){
		int userId = authController.getIdInToken(auth); // id in token
		int ownerId = usersController.getUserId(id); // id of owner of the recipe

		if(userId != ownerId) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
		}

		boolean result;

		result = recipesController.updateRecipe(id, recipe);

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
		int userId = authController.getIdInToken(auth); // id in token
		int ownerId = usersController.getUserId(id); // id of owner of the recipe

		if(userId != ownerId) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
		}

		recipesController.deleteRecipe(id);

		return Response.noContent().build();
	}
}