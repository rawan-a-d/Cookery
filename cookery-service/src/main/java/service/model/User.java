package service.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private static int idSeeder = 0;
    private String name;
    private String email;
    private String password;
    private List<Recipe> favouriteRecipes;

    public User() {
        this.id = idSeeder;
        idSeeder++;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = idSeeder;
        idSeeder++;
    }

    public User(int id, String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.favouriteRecipes = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void decreaseIdSeeder() {
        idSeeder--;
    }

    public List<Recipe> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public void setFavouriteRecipes(List<Recipe> favouriteRecipes) {
        this.favouriteRecipes = favouriteRecipes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
