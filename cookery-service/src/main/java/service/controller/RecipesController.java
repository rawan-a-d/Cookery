package service.controller;

import service.model.DTO.RecipeDTO;
import service.model.Recipe;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;

import java.sql.SQLException;
import java.util.List;

public class RecipesController {
    // ---------------------------------------------------- Recipes ------------------------------------------------------------
    public List<Recipe> getRecipes() {

        RecipesRepository recipesRepository = new RecipesRepository();


        System.out.println("Recipes:");
        List<Recipe> recipes;
        try {
            recipes = recipesRepository.getRecipes();

            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Recipe getRecipe(int id) {
        RecipesRepository recipesRepository = new RecipesRepository();

        Recipe recipe;
        try {
            recipe = recipesRepository.getRecipe(id);
            return recipe;
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Recipe> getRecipes(int userId) {
        RecipesRepository recipesRepository = new RecipesRepository();

        System.out.println("Recipes:");
        List<Recipe> recipes;
        try {
            recipes = recipesRepository.getRecipes(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<RecipeDTO> getRecipesDTO(int userId) {
        RecipesRepository recipesRepository = new RecipesRepository();

        System.out.println("Recipes:");
        List<RecipeDTO> recipes;
        try {
            recipes = recipesRepository.getRecipesDTO(userId);
            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<Recipe> getRecipes(String ingredient) {
        RecipesRepository recipesRepository = new RecipesRepository();

        List<Recipe> recipes;
        try {
            recipes = recipesRepository.getRecipes(ingredient);

            return recipes;
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public boolean updateRecipe(int id, Recipe recipe) {
        RecipesRepository recipesRepository = new RecipesRepository();

        try {
            return recipesRepository.updateRecipe(id, recipe);
        }
        catch (CookeryDatabaseException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void createRecipe(Recipe recipe) {
        RecipesRepository recipesRepository = new RecipesRepository();

        try {
            recipesRepository.createRecipe(recipe);
        }
        catch (CookeryDatabaseException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipe(int id) {
        RecipesRepository recipesRepository = new RecipesRepository();

        try {
            recipesRepository.deleteRecipe(id);
        }
        catch (CookeryDatabaseException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
