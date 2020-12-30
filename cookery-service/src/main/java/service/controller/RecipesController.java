package service.controller;

import service.model.DTO.RecipeDTO;
import service.model.DTO.RecipeFollowDTO;
import service.model.Recipe;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class RecipesController {
    private final static Logger LOGGER = Logger.getLogger(RecipesController.class.getName());
    RecipesRepository recipesRepository = new RecipesRepository();

    // ---------------------------------------------------- Recipes ------------------------------------------------------------
    public List<Recipe> getRecipes() {


        List<Recipe> recipes = null;
        try {
            recipes = recipesRepository.getRecipes();
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }

    public Recipe getRecipe(int id) {
        Recipe recipe = null;
        try {
            recipe = recipesRepository.getRecipe(id);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipe;
    }

    public RecipeFollowDTO getRecipeFollow(int recipeId, int userId) {
        RecipeFollowDTO recipe = null;
        try {
            recipe = recipesRepository.getRecipeFollow(recipeId, userId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipe;
    }

    public List<Recipe> getRecipes(int userId) {
        List<Recipe> recipes = null;
        try {
            recipes = recipesRepository.getRecipes(userId);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }


    // ???????????????????????????????????????????????????
    public List<RecipeDTO> getRecipes(int userId, String ingredient) {
        List<RecipeDTO> recipes = null;
        try {
            recipes = recipesRepository.getRecipes(userId, ingredient);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }

    public List<RecipeDTO> getRecipesDTO(int userId) {
        List<RecipeDTO> recipes;
        try {
            recipes = recipesRepository.getRecipesDTO(userId);
            return recipes;
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }


    public List<RecipeDTO> getFavouritesDTO(int userId) {

        List<RecipeDTO> recipes = null;
        try {
            recipes = recipesRepository.getFavouritesDTO(userId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }


    public boolean updateRecipe(int id, Recipe recipe) {

        try {
            return recipesRepository.updateRecipe(id, recipe);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return false;
    }

    public boolean createRecipe(Recipe recipe) {
        boolean result = false;
        try {
            result = recipesRepository.createRecipe(recipe);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return result;
    }

    public boolean deleteRecipe(int id) {
        boolean result = false;
        try {
            result = recipesRepository.deleteRecipe(id);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }



    public boolean addFavourite(int userId, RecipeDTO favourite) {

        try {
            return recipesRepository.addFavourite(userId, favourite);
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public boolean removeFavourite(int favouriteId) {
        boolean result = false;
        try {
            result = recipesRepository.removeFavourite(favouriteId);
        }
        catch (CookeryDatabaseException | SQLException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }


    public List<Recipe> getFavourites(int userId) {

        List<Recipe> recipes;
        try {
            recipes = recipesRepository.getFavourites(userId);
            return recipes;
        }
        catch (CookeryDatabaseException | URISyntaxException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }

}
