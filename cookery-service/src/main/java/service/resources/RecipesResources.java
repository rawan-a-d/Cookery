package service.resources;

import cyclops.control.Validated;
import service.controller.AuthController;
import service.controller.RecipesController;
import service.controller.UsersController;
import service.model.DTO.RecipeDTO;
import service.model.DTO.RecipeFollowDTO;
import service.model.DTO.UserDTO;
import service.model.Recipe;
import service.model.Role;
import service.validators.Error;
import service.validators.RecipeValidator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/recipes")
public class RecipesResources {

	@Context
	private UriInfo uriInfo;

	private final RecipesController recipesController = new RecipesController();

	@GET //GET at http://localhost:XXXX/recipes?ingredient=onion OR http://localhost:XXXX/recipes
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipesByIngredient(@DefaultValue("all") @QueryParam("ingredient") List<String> ingredients, @HeaderParam("Authorization") String auth){
		List<RecipeDTO> recipes;

		int userId = -1;

		if(auth != null) { // auth exists
			userId = AuthController.getIdInToken(auth);
		}

		if(ingredients.get(0).equals("all")) {
			recipes = recipesController.getRecipesDTO(userId);
		}
		else {
			recipes = recipesController.getRecipes(userId, ingredients);
		}

		GenericEntity<List<RecipeDTO>> entity = new GenericEntity<List<RecipeDTO>>(recipes){ };
		return Response.ok(entity).build();
	}

	@GET //GET at http://localhost:XXXX/recipes/1
	@Path("v1/{id}")
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


	@GET //GET at http://localhost:XXXX/recipes/v2/1 (get recipes with follow status)
	@Path("v2/{id}")
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecipeFollow(@PathParam("id") int id, @HeaderParam("Authorization") String auth) {
		int userId = -1;
		if(auth != null) { // auth exists
			UserDTO user = AuthController.getUser(auth); // user in token
			userId = user.getId();
		}

		RecipeFollowDTO recipe = recipesController.getRecipeFollow(id, userId);
		if(recipe != null){
			return Response.ok(recipe).build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid recipe id").build();
		}
	}


	@POST //POST at http://localhost:XXXX/recipes
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({"user", "admin"}) // allow for logged in users (admins, users)
	public Response addRecipe(Recipe recipe, @HeaderParam("Authorization") String auth){
		int userId = -1;

		// Validate data
		Validated<Error, String> name = RecipeValidator.validName(recipe);;
		Validated<Error, String> description = RecipeValidator.validDescription(recipe);
		Validated<Error, String> ingredients = RecipeValidator.validIngredients(recipe);

		// if name is invalid , if email is invalid, if password is invalid -> incorrect data
		if(name.isInvalid() || description.isInvalid() || ingredients.isInvalid()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
		}
		else {
			userId = AuthController.getIdInToken(auth);

			if(recipe.getUserId() == userId) { // Same user
				recipesController.createRecipe(recipe);

				String url = uriInfo.getAbsolutePath() + "/" + recipe.getId();
				URI uri = URI.create(url);
				return Response.created(uri).build();
			}
			else { // Another user
				return Response.status(Response.Status.FORBIDDEN)
						.entity("You are not allowed to perform this action").build();
			}
		}
	}

	// ASYNC
	@PUT //PUT at http://localhost:XXXX/recipes/3
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	@RolesAllowed({"user", "admin"}) // Allowed only for owners
	public void updateRecipe(@PathParam("id") int id, Recipe recipe, @HeaderParam("Authorization") String auth, @Suspended final AsyncResponse asyncResponse){
		new Thread((new Runnable() {
			@Override
			public void run() {
				Response response = update();
				asyncResponse.resume(response);
			}

			private Response update() {
				UserDTO user = AuthController.getUser(auth); // user in token
				int ownerId = UsersController.getUserId(id); // id of owner of the recipe

				if(user.getId() != ownerId) {
					return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
				}

				// Validate data
				final Validated<Error, String> name = RecipeValidator.validName(recipe);;
				final Validated<Error, String> description = RecipeValidator.validDescription(recipe);
				final Validated<Error, String> ingredients = RecipeValidator.validIngredients(recipe);

				boolean result = recipesController.updateRecipe(id, recipe);
				// if name is invalid , if email is invalid, if password is invalid -> incorrect data
				if(name != null && name.isInvalid() || description != null && description.isInvalid() || ingredients != null && ingredients.isInvalid()) {
					return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
				}
				else {
					if (result) {
						return Response.noContent().build();
					} else {
						return Response.status(Response.Status.NOT_FOUND).entity("Please provide a valid recipe id").build();
					}
				}
			}
		})).start();
	}

	@DELETE //DELETE at http://localhost:XXXX/recipes/3
	@Path("{id}")
	@RolesAllowed({"user", "admin"}) // Allow for owners and admins
	public Response deleteRecipe(@PathParam("id") int id, @HeaderParam("Authorization") String auth){
		UserDTO user = AuthController.getUser(auth); // user in token
		int ownerId = UsersController.getUserId(id); // id of owner of the recipe

		if(user.getId() != ownerId && user.getRole() != Role.valueOf("admin")) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to perform this action").build();
		}

		recipesController.deleteRecipe(id);

		return Response.noContent().build();
	}
}