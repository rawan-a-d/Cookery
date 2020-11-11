package service.model;

public class Ingredient {
	private int id;
	private String ingredient;
	private int amount;
	// ADDED
//	private int recipeId;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "RECIPE_ID_FK")
//	private Recipe recipe;
//	private Recipe recipe;

	public Ingredient() {

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
