package service.model;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ingredient that = (Ingredient) o;
		return id == that.id &&
				amount == that.amount &&
				Objects.equals(ingredient, that.ingredient);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, ingredient, amount);
	}

	//	public int getRecipeId() {
//		return recipeId;
//	}

	//public void setRecipeId(int recipeId) {
//		this.recipeId = recipeId;
//	}
}
