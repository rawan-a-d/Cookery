package service.model.DTO;

import service.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeFollowDTO {
    private int id;
    private String name;
    private String image;
    private String description;
    private List<Ingredient> ingredients;
    private UserDTO user;
    private int favouriteId;
    private boolean isFavourite;
    private int followId;
    private boolean isFollowed;


    public RecipeFollowDTO(int id, String name, String image, String description,
                           UserDTO user, int favouriteId, boolean isFavourite, int followId, boolean isFollowed) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.user = user;
        this.favouriteId = favouriteId;
        this.isFavourite = isFavourite;
        this.followId = followId;
        this.isFollowed = isFollowed;
        this.ingredients = new ArrayList<>();
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean followed) {
        isFollowed = followed;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
