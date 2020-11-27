import org.junit.Test;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.User;
import service.repository.DataStore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class RecipeTest {
	private final DataStore dataStore = DataStore.getInstance();

	@Test
	public void createRecipe(){
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("olives", 5));

		Recipe recipe = new Recipe("recipe test", "image.jpeg","recipe test desc", 1, ingredients);

		// create recipe
		dataStore.addRecipe(recipe);

		// retrieve recipe
		Recipe retrievedRecipe = dataStore.getRecipe(recipe.getId());

		assertSame(recipe, retrievedRecipe);
	}

	@Test
	public void getRecipe(){
		Recipe recipe = dataStore.getRecipe(2);

		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("minced garlic", 2));
		ingredients.add(new Ingredient("brown sugar", 2));
		ingredients.add(new Ingredient("soy sauce", 5));
		ingredients.add(new Ingredient("onion", 1/4));
		ingredients.add(new Ingredient("sesame oil", 2));
		ingredients.add(new Ingredient("sesame seeds", 1));

		Recipe expectedRecipe = new Recipe("Skillet Chicken Bulgogi", "https://i.pinimg.com/originals/65/c2/63/65c26342ad2bc80eacfd6062646355b1.jpg", "Step 1\n" +
				"Whisk onion, soy sauce, brown sugar, garlic, sesame oil, sesame seeds, cayenne pepper, salt, and black pepper together in a bowl until marinade is smooth.\n" +
				"\n" +
				" Step 2\n" +
				"Cook and stir chicken and marinade together in a large skillet over medium-high heat until chicken is cooked through, about 15 minutes.", 3, ingredients);

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
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("zucchini", 10));
		ingredients.add(new Ingredient("tomato paste", 2));
		ingredients.add(new Ingredient(" long-grain rice", 1));
		ingredients.add(new Ingredient("onion", 1/2));

		Recipe updatedRecipe = new Recipe("Crab Cakes", "img.png", "Step 1\n" +
				"In a medium bowl, whisk together egg, mayonnaise, lemon juice, red pepper flakes, tarragon, and scallions. Gently stir in crabmeat, being careful not to break up meat. Gradually mix in cracker crumbs, adding until desired consistency is achieved.\n" +
				"\n" +
				" Step 2\n" +
				"Heat butter in a skillet over medium heat. Form crab mixture into 4 patties. Place patties in skillet, and cook until golden brown, about 5 to 6 minutes on each side.", 2, ingredients);

		// delete recipe
		dataStore.updateRecipe(1, updatedRecipe);

		// retrieve recipe
		Recipe recipe = dataStore.getRecipe(1);

		assertSame(updatedRecipe, recipe);
	}


	@Test
	public void getRecipesBy(){
		List<Recipe> actual = dataStore.getRecipesBy("onion");

		List<Recipe> expected = new ArrayList<>();
		List<Ingredient> recipe2Ingredients = new ArrayList<>();
		recipe2Ingredients.add(new Ingredient("zucchini", 10));
		recipe2Ingredients.add(new Ingredient("tomato paste", 2));
		recipe2Ingredients.add(new Ingredient(" long-grain rice", 1));
		recipe2Ingredients.add(new Ingredient("onion", 1/2));

		List<Ingredient> recipe3Ingredients = new ArrayList<>();
		recipe3Ingredients.add(new Ingredient("minced garlic", 2));
		recipe3Ingredients.add(new Ingredient("brown sugar", 2));
		recipe3Ingredients.add(new Ingredient("soy sauce", 5));
		recipe3Ingredients.add(new Ingredient("onion", 1/4));
		recipe3Ingredients.add(new Ingredient("sesame oil", 2));
		recipe3Ingredients.add(new Ingredient("sesame seeds", 1));


		Recipe recipe = new Recipe("Stuffed zucchini (kousa mahshi)", "https://i.pinimg.com/originals/70/f0/b8/70f0b8d52d9ed3c9c84607cd79dd0065.jpg", "Cut off the zucchini " +
				"stalks, then slice off the dried tips at the opposite ends without removing too much flesh. " +
				"Carefully hollow out the zucchini from the stalk end by pushing and turning a manakra into the flesh. " +
				"The tool will remove thin fingers of flesh at a time; keep hollowing until you have a generous cavity. " +
				"(Reserve the zucchini flesh for another purpose, such as an omelette.) Fill a bowl with water and add 1 tsp salt. " +
				"Wash the zucchini in the salted water (this helps to keep them firm when cooking), then drain.\n" +
				"\n" +
				"Fill each zucchini with the stuffing, leaving 1 cm free at the top to allow the filling to expand. " +
				"It’s easiest to fill the zucchini by hand, tapping them on the bench every now and then to settle the stuffing down. " +
				"If you have any leftover stuffing, shape it into meatballs.\n" +
				"\n" +
				"Fill a large saucepan with water and add 2 tbsp salt and the tomato paste. Add the stuffed zucchini and any meatballs and bring to the boil. Simmer over low heat for about 1 hour, allowing the sauce to reduce. " +
				"Serve the stuffed zucchini with a little of the sauce and a dollop of yoghurt.", 2, recipe2Ingredients);
		Recipe recipe2 = new Recipe("Skillet Chicken Bulgogi", "https://i.pinimg.com/originals/65/c2/63/65c26342ad2bc80eacfd6062646355b1.jpg", "Step 1\n" +
				"Whisk onion, soy sauce, brown sugar, garlic, sesame oil, sesame seeds, cayenne pepper, salt, and black pepper together in a bowl until marinade is smooth.\n" +
				"\n" +
				" Step 2\n" +
				"Cook and stir chicken and marinade together in a large skillet over medium-high heat until chicken is cooked through, about 15 minutes.", 3, recipe3Ingredients);

		expected.add(recipe);
		expected.add(recipe2);

//		assertArrayEquals(expected.toArray(), actual.toArray());
		assertSame(expected.get(0).getName(), actual.get(0).getName());
		assertSame(expected.get(0).getImage(), actual.get(0).getImage());
		assertSame(expected.get(0).getDescription(), actual.get(0).getDescription());
		assertSame(expected.get(0).getIngredients().get(0).getIngredient(), actual.get(0).getIngredients().get(0).getIngredient());

		assertSame(expected.get(1).getName(), actual.get(1).getName());
		assertSame(expected.get(1).getImage(), actual.get(1).getImage());
		assertSame(expected.get(1).getDescription(), actual.get(1).getDescription());
		assertSame(expected.get(1).getIngredients().get(0).getIngredient(), actual.get(1).getIngredients().get(0).getIngredient());
	}


	@Test
	public void getUserRecipes(){
		List<Recipe> actual = dataStore.getUserRecipes(1);

		List<Recipe> expected = new ArrayList<>();
		User user = new User("Ranim", "ranim@gmail.com", "12345");


		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("ricotta cheese", 2));
		ingredients.add(new Ingredient("egg", 1));
		ingredients.add(new Ingredient("Italian seasoning", 2));
		ingredients.add(new Ingredient("sausage", 1));
		ingredients.add(new Ingredient("jar marinara sauce", 2));
		
		Recipe recipe = new Recipe("Lasagna Flatbread\n", "https://static.toiimg.com/thumb/60716791.cms?imgsize=631011&width=800&height=800", "Step 1\n" +
				"Preheat oven to 375 degrees F (190 degrees C).\n" +
				"\n" +
				" Step 2\n" +
				"Combine ricotta cheese, 1/2 of the mozzarella cheese, Parmesan cheese, egg, and Italian seasoning in a bowl.\n" +
				"\n" +
				" Step 3\n" +
				"Cook sausage in a skillet over medium heat until no longer pink, 5 to 10 minutes; drain. Stir in marinara sauce.\n" +
				"\n" +
				" Step 4\n" +
				"Spread 1/6 of the cheese mixture evenly on each flatbread; cover with sausage mixture. Top with remaining mozzarella cheese.\n" +
				"\n" +
				" Step 5\n" +
				"Bake in the preheated oven until cheese is melted and bubbly, 10 to 15 minutes.", 1, ingredients);

		expected.add(recipe);

//		assertArrayEquals(expected.toArray(), actual.toArray());
		assertSame(expected.size(), actual.size());
		assertSame(expected.get(0).getName(), actual.get(0).getName());
		assertSame(expected.get(0).getImage(), actual.get(0).getImage());
		assertSame(expected.get(0).getDescription(), actual.get(0).getDescription());
		assertSame(expected.get(0).getIngredients().get(0).getIngredient(), actual.get(0).getIngredients().get(0).getIngredient());
	}
}
