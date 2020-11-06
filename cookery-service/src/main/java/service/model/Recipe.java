package service.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	private int id;
	private static int idSeeder = 0;
	private String name;
	private String image;
//	private File image;
	private String description;
	private List<Ingredient> ingredients;
	private int userId;

//	private User user;

	public Recipe() {
		this.id = idSeeder;
		idSeeder++;
	}

	public Recipe(String name, String image, String description, int userId, List<Ingredient> ingredients) {
		this.id = idSeeder;
		idSeeder++;
		this.name = name;
		this.image = image;
		this.description = description;
		this.userId = userId;
		this.ingredients = ingredients;
	}

//	public Recipe(String name, String image, String description, List<Ingredient> ingredients, int userId) {
//		this.id = idSeeder;
//		idSeeder++;
//		this.name = name;
//		this.image = image;
//		this.description = description;
//		this.userId = userId;
//		this.ingredients = ingredients;
//	}

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

//	public Recipe(String name, File image, String description, int userId, List<Ingredient> ingredients) {
//		this.id = idSeeder;
//		idSeeder++;
//		this.name = name;
//		this.image = image;
//		this.description = description;
//		this.userId = userId;
//		this.ingredients = ingredients;
//
//		System.out.println("image");
//		System.out.println(this.image);
//	}

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

//
//	public Image getImage() {
//		return image;
//	}
//
//	public void setImage(Image image) {
//		this.image = image;
//	}
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



//	public int getUserId() {
//		return userId;
//	}
//
//	public void setUserId(int userId) {
//		this.userId = userId;
//	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public static void decreaseIdSeeder() {
		idSeeder--;
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
}