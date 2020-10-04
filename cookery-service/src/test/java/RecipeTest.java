import org.junit.Test;
import service.model.Recipe;
import service.repository.DataStore;

import static org.junit.jupiter.api.Assertions.*;


public class RecipeTest {
	private final DataStore dataStore = DataStore.getInstance();

	@Test
	public void createRecipe(){
		Recipe recipe = new Recipe("recipe test", "image.jpeg","recipe test desc", 1);

		// create recipe
		dataStore.addRecipe(recipe);

		// retrieve recipe
		Recipe retrievedRecipe = dataStore.getRecipe(recipe.getId());

		assertSame(recipe, retrievedRecipe);
	}

	@Test
	public void getRecipe(){
		Recipe recipe = dataStore.getRecipe(2);

		Recipe expectedRecipe = new Recipe("Skillet Chicken Bulgogi", "img.png", "Step 1\n" +
				"Whisk onion, soy sauce, brown sugar, garlic, sesame oil, sesame seeds, cayenne pepper, salt, and black pepper together in a bowl until marinade is smooth.\n" +
				"\n" +
				" Step 2\n" +
				"Cook and stir chicken and marinade together in a large skillet over medium-high heat until chicken is cooked through, about 15 minutes.", 3);

		assertEquals(expectedRecipe.getName(), recipe.getName());
		assertEquals(expectedRecipe.getDescription(), recipe.getDescription());
		assertEquals(expectedRecipe.getImage(), recipe.getImage());
	}


	@Test
	public void deleteRecipe(){
		// delete recipe
		boolean isRecipeDeleted = dataStore.deleteRecipe(3);

		// retrieve recipe
		Recipe recipe = dataStore.getRecipe(3);

		assertTrue(isRecipeDeleted && recipe == null);
	}

	@Test
	public void updateRecipe(){

		Recipe updatedRecipe = new Recipe("Crab Cakes", "img.png", "Step 1\n" +
				"In a medium bowl, whisk together egg, mayonnaise, lemon juice, red pepper flakes, tarragon, and scallions. Gently stir in crabmeat, being careful not to break up meat. Gradually mix in cracker crumbs, adding until desired consistency is achieved.\n" +
				"\n" +
				" Step 2\n" +
				"Heat butter in a skillet over medium heat. Form crab mixture into 4 patties. Place patties in skillet, and cook until golden brown, about 5 to 6 minutes on each side.", 2);

		// delete recipe
		dataStore.updateRecipe(1, updatedRecipe);

		// retrieve recipe
		Recipe recipe = dataStore.getRecipe(1);

		assertSame(updatedRecipe, recipe);
	}
}
