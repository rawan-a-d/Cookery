package service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {
	private int id;
	private String name;
	private String image;
	private String description;
	private List<Ingredient> ingredients;
	private int userId;


	public Recipe() {

	}

	public Recipe(String name, String image, String description, int userId) {
		this.name = name;
		this.image = image;
		this.description = description;
		this.userId = userId;
	}

	public Recipe(String name, String image, String description, int userId, List<Ingredient> ingredients) {
		this.name = name;
		this.image = image;
		this.description = description;
		this.userId = userId;
		this.ingredients = ingredients;
	}

	public Recipe(int id, String name, String image, String description, int userId) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.description = description;
		this.userId = userId;
		this.ingredients = new ArrayList<>();
	}

	public Recipe(int id, String name, String image, String description, List<Ingredient> ingredients, int userId) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.description = description;
		this.userId = userId;
		this.ingredients = ingredients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}


	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "Recipe{" +
				"id=" + id +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", description='" + description + '\'' +
				", ingredients=" + ingredients +
				", userId=" + userId +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Recipe recipe = (Recipe) o;
		return id == recipe.id &&
				userId == recipe.userId &&
				Objects.equals(name, recipe.name) &&
				Objects.equals(image, recipe.image) &&
				Objects.equals(description, recipe.description) &&
				Objects.equals(ingredients, recipe.ingredients);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, image, description, ingredients, userId);
	}
}