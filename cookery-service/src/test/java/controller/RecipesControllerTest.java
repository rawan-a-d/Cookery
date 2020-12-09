package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.RecipesController;
import service.model.DTO.RecipeDTO;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.User;
import service.repository.CookeryDatabaseException;
import service.repository.RecipesRepository;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipesControllerTest {
    @InjectMocks
    RecipesController recipesController;

    @Mock
    RecipesRepository recipesRepository;


    @Test
    public void getRecipes() throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> expectedRecipes = Arrays.asList(
                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                        Arrays.asList(
                                new Ingredient(1, "onion", 2),
                                new Ingredient(2, "garlic", 1)
                        ), 1),
                new Recipe(2, "recipe 2", "recipe 2 image","recipe 2 desc",
                        Arrays.asList(
                                new Ingredient(3, "tomato", 2)
                        ),1),
                new Recipe(3, "recipe 3", "recipe 3 image","recipe 3 desc", 2),
                new Recipe(4, "recipe 4", "recipe 4 image","recipe 4 desc",
                        Arrays.asList(
                                new Ingredient(4, "onion", 5)
                        ),3)
        );

        when(recipesRepository.getRecipes()).thenReturn(
                Arrays.asList(
                        new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                                Arrays.asList(
                                        new Ingredient(1, "onion", 2),
                                        new Ingredient(2, "garlic", 1)
                                ), 1),
                        new Recipe(2, "recipe 2", "recipe 2 image","recipe 2 desc",
                                Arrays.asList(
                                        new Ingredient(3, "tomato", 2)
                                ),1),
                        new Recipe(3, "recipe 3", "recipe 3 image","recipe 3 desc", 2),
                        new Recipe(4, "recipe 4", "recipe 4 image","recipe 4 desc",
                                Arrays.asList(
                                        new Ingredient(4, "onion", 5)
                                ),3)
                )
        );

        List<Recipe> actualRecipes = recipesController.getRecipes();

        assertEquals(expectedRecipes.size(), actualRecipes.size());
        assertArrayEquals(expectedRecipes.toArray(), actualRecipes.toArray()); // in order to use this (equals need to be implemented in User)
    }


    @Test
    public void getRecipesByIngredient() throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> expectedRecipes = Arrays.asList(
                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc", 1),
                new Recipe(4, "recipe 4", "recipe 4 image","recipe 4 desc", 3)
        );

        when(recipesRepository.getRecipes("onion")).thenReturn(
                Arrays.asList(
                        new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc", 1),
                        new Recipe(4, "recipe 4", "recipe 4 image","recipe 4 desc", 3)
                )
        );

        List<Recipe> actualRecipes = recipesController.getRecipes("onion");

        assertEquals(expectedRecipes.size(), actualRecipes.size());
        assertArrayEquals(expectedRecipes.toArray(), actualRecipes.toArray());
    }


    @Test
    public void getRecipesByIngredient_notFound_returnsEmptyArray() throws CookeryDatabaseException, URISyntaxException {
        when(recipesRepository.getRecipes("tomato sauce")).thenReturn(
                Arrays.asList()
        );

        assertEquals(0, recipesController.getRecipes("tomato sauce").size());
    }


    @Test
    public void getRecipesOfUser() throws CookeryDatabaseException, URISyntaxException {
        // returns all recipes, specify which of them are favourites
        List<Recipe> expectedRecipes =  Arrays.asList(
                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                        Arrays.asList(
                                new Ingredient(1, "onion", 2),
                                new Ingredient(2, "garlic", 1)
                        ), 1),
                new Recipe(2, "recipe 2", "recipe 2 image", "recipe 2 desc",
                        Arrays.asList(
                                new Ingredient(3, "tomato", 2)
                        ),1)
        );

        when(recipesRepository.getRecipes(1)).thenReturn(
                Arrays.asList(
                        new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                                Arrays.asList(
                                        new Ingredient(1, "onion", 2),
                                        new Ingredient(2, "garlic", 1)
                                ), 1),
                        new Recipe(2, "recipe 2", "recipe 2 image", "recipe 2 desc",
                                Arrays.asList(
                                        new Ingredient(3, "tomato", 2)
                                ),1)
                )
        );

        List<Recipe> actualRecipes = recipesController.getRecipes(1);

        assertEquals(expectedRecipes.size(), actualRecipes.size());
        assertArrayEquals(expectedRecipes.toArray(), actualRecipes.toArray()); // in order to use this (equals need to be implemented in User)
    }



    @Test
    public void getRecipesDTO() throws CookeryDatabaseException, URISyntaxException {
        List<RecipeDTO> expectedRecipes =  Arrays.asList(
                new RecipeDTO(1, "recipe 1", "recipe 1 image",
                        new User(1, "Rawan")),
                new RecipeDTO(2, "recipe 2", "recipe 2 image",
                        new User(1, "Rawan"), 1, true),
                new RecipeDTO(3, "recipe 3", "recipe 3 image",
                        new User(2, "Anas"), 2, true),
                new RecipeDTO(4, "recipe 4", "recipe 4 image",
                        new User(3, "Omar"))
        );

        when(recipesRepository.getRecipesDTO(1)).thenReturn(
                Arrays.asList(
                        new RecipeDTO(1, "recipe 1", "recipe 1 image",
                                new User(1, "Rawan")),
                        new RecipeDTO(2, "recipe 2", "recipe 2 image",
                                new User(1, "Rawan"), 1, true),
                        new RecipeDTO(3, "recipe 3", "recipe 3 image",
                                new User(2, "Anas"), 2, true),
                        new RecipeDTO(4, "recipe 4", "recipe 4 image",
                                new User(3, "Omar"))
                )
        );

        List<RecipeDTO> actualRecipes = recipesController.getRecipesDTO(1);

        assertEquals(expectedRecipes.size(), actualRecipes.size());
        assertArrayEquals(expectedRecipes.toArray(), actualRecipes.toArray()); // in order to use this (equals need to be implemented in User)
    }

    @Test
    public void getRecipesDTO_invalidId_returnsAll() throws CookeryDatabaseException, URISyntaxException {
        List<RecipeDTO> expectedRecipes =  Arrays.asList(
                new RecipeDTO(1, "recipe 1", "recipe 1 image",
                        new User(1, "Rawan")),
                new RecipeDTO(2, "recipe 2", "recipe 2 image",
                        new User(1, "Rawan")),
                new RecipeDTO(3, "recipe 3", "recipe 3 image",
                        new User(2, "Anas")),
                new RecipeDTO(4, "recipe 4", "recipe 4 image",
                        new User(3, "Omar"))
        );

        when(recipesRepository.getRecipesDTO(-1)).thenReturn(
            Arrays.asList(
                    new RecipeDTO(1, "recipe 1", "recipe 1 image",
                            new User(1, "Rawan")),
                    new RecipeDTO(2, "recipe 2", "recipe 2 image",
                            new User(1, "Rawan")),
                    new RecipeDTO(3, "recipe 3", "recipe 3 image",
                            new User(2, "Anas")),
                    new RecipeDTO(4, "recipe 4", "recipe 4 image",
                            new User(3, "Omar"))
            )
        );

        List<RecipeDTO> actualRecipes = recipesController.getRecipesDTO(-1);

        assertEquals(expectedRecipes.size(), actualRecipes.size());
        assertArrayEquals(expectedRecipes.toArray(), actualRecipes.toArray()); // in order to use this (equals need to be implemented in User)
    }


    @Test
    public void getRecipe() throws CookeryDatabaseException, URISyntaxException {
        Recipe expectedRecipe = new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                Arrays.asList(
                        new Ingredient(1, "onion", 2),
                        new Ingredient(2, "garlic", 1)
                ), 1);

        when(recipesRepository.getRecipe(1)).thenReturn(
                new Recipe(1, "recipe 1", "recipe 1 image", "recipe 1 desc",
                        Arrays.asList(
                                new Ingredient(1, "onion", 2),
                                new Ingredient(2, "garlic", 1)
                        ), 1)
        );

        Recipe actualRecipe = recipesController.getRecipe(1);

        assertEquals(expectedRecipe, actualRecipe); // in order to use this (equals need to be implemented in User)
    }



    @Test
    public void deleteRecipe() throws SQLException, CookeryDatabaseException, URISyntaxException {
        when(recipesRepository.deleteRecipe(1)).thenReturn(true);

        boolean result = recipesController.deleteRecipe(1);

        assertTrue(result);
    }


    @Test
    public void updateRecipe() throws SQLException, CookeryDatabaseException {
        Recipe updatedRecipe = new Recipe(1, "recipe 1 updated", "recipe 1 image", "recipe 1 desc",
                Arrays.asList(
                        new Ingredient("ingredient 1", 2),
                        new Ingredient("ingredient 3", 6)
                ), 1);

        when(recipesRepository.updateRecipe(1, updatedRecipe)).thenReturn(true);

        boolean result = recipesController.updateRecipe(1, updatedRecipe);

        assertTrue(result);
    }


    @Test
    public void createRecipe() throws SQLException, CookeryDatabaseException, URISyntaxException {
        Recipe newRecipe = new Recipe("recipe 5", "recipe 5 image", "recipe 5 desc", 3,
                Arrays.asList(
                        new Ingredient("ingredient 4", 2),
                        new Ingredient("ingredient 5", 3)
                ));

        when(recipesRepository.createRecipe(newRecipe)).thenReturn(true);

        boolean result = recipesController.createRecipe(newRecipe);

        assertTrue(result);
    }


    @Test
    public void getFavourites() throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> expectedRecipes =  Arrays.asList(
                new Recipe(2, "recipe 2", "recipe 2 image", "recipe 2 desc", 1),
                new Recipe(3, "recipe 3", "recipe 3 image", "recipe 3 desc", 2)
        );

        when(recipesRepository.getFavourites(1)).thenReturn(
                Arrays.asList(
                        new Recipe(2, "recipe 2", "recipe 2 image", "recipe 2 desc", 1),
                        new Recipe(3, "recipe 3", "recipe 3 image", "recipe 3 desc", 2)
                )
        );

        List<Recipe> actualRecipes = recipesController.getFavourites(1);

        assertEquals(expectedRecipes, actualRecipes);
    }


    @Test
    public void getFavouritesDTO() throws Exception {
        List<RecipeDTO> expectedRecipes =  Arrays.asList(
                new RecipeDTO(2, "recipe 2", "recipe 2 image",
                        new User(1, "Rawan")),
                new RecipeDTO(3, "recipe 3", "recipe 3 image",
                        new User(2, "Anas"))
        );

        when(recipesRepository.getFavouritesDTO(1)).thenReturn(
                Arrays.asList(
                        new RecipeDTO(2, "recipe 2", "recipe 2 image",
                                new User(1, "Rawan")),
                        new RecipeDTO(3, "recipe 3", "recipe 3 image",
                                new User(2, "Anas"))
                )
        );

        List<RecipeDTO> actualRecipes = recipesController.getFavouritesDTO(1);

        assertEquals(expectedRecipes, actualRecipes);
        assertEquals(expectedRecipes.size(), actualRecipes.size());
    }


    @Test
    public void addFavourite() throws CookeryDatabaseException, SQLException, URISyntaxException {
        RecipeDTO recipe = new RecipeDTO(4, new User(4, "Raneem"));

        when(recipesRepository.addFavourite(4, recipe)).thenReturn(true);

        boolean result = recipesController.addFavourite(4, recipe);

        assertTrue(result);
    }


    @Test
    public void removeFavourite() throws CookeryDatabaseException, SQLException, URISyntaxException {
        when(recipesRepository.removeFavourite(2)).thenReturn(true);

        boolean result = recipesController.removeFavourite(2);

        assertTrue(result);
    }
}
