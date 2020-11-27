package service.model;

public class Ingredient {
	private int id;
	private static int idSeeder = 0;
	private String ingredient;
	private int amount;

	public Ingredient() {
		this.id = idSeeder;
		idSeeder++;
	}

	public Ingredient(String name, int amount) {
		this.ingredient = name;
		this.amount = amount;
	}

	public Ingredient(int id, String ingredient, int amount) {
		this.id = id;
		this.ingredient = ingredient;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIngredient() {
		return ingredient;
	}

	public int getAmount() {
		return amount;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Ingredient{" +
				"id=" + id +
				", ingredient='" + ingredient + '\'' +
				", amount=" + amount +
				'}';
	}


	//	public int getRecipeId() {
//		return recipeId;
//	}

	//public void setRecipeId(int recipeId) {
//		this.recipeId = recipeId;
//	}
}
