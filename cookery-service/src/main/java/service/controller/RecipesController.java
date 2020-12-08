package service.controller;

import service.model.DTO.RecipeDTO;
import service.model.Recipe;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;

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
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }

    public Recipe getRecipe(int id) {

        Recipe recipe = null;
        try {
            recipe = recipesRepository.getRecipe(id);
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
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return recipes;
    }


    public List<RecipeDTO> getFavouritesDTO(int userId) {

        List<RecipeDTO> recipes = null;
        try {
            recipes = recipesRepository.getFavouritesDTO(userId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            System.out.println(ex.getMessage());

        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            System.out.println(ex.getMessage());
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

    public void createRecipe(Recipe recipe) {

        try {
            recipesRepository.createRecipe(recipe);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
    }

    public void deleteRecipe(int id) {

        try {
            recipesRepository.deleteRecipe(id);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
    }

    public List<RecipeDTO> getRecipesDTO(int userId) {

        List<RecipeDTO> recipes;
        try {
            recipes = recipesRepository.getRecipesDTO(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }



    public boolean addFavourite(int userId, RecipeDTO favourite) {

        try {
            return recipesRepository.addFavourite(userId, favourite);
        }
        catch (CookeryDatabaseException | SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return false;
        }
    }


    public void removeFavourite(int favouriteId) {

        try {
            recipesRepository.removeFavourite(favouriteId);
        }
        catch (CookeryDatabaseException | SQLException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
    }


    public List<Recipe> getFavourites(int userId) {

        List<Recipe> recipes;
        try {
            recipes = recipesRepository.getFavourites(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
            return null;
        }
    }

}
