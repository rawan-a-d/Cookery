package service.model;

public class Ingredient {
	private String name;
	private int amount;

	public Ingredient() {

	}

	public Ingredient(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public int getAmount() {
		return amount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
