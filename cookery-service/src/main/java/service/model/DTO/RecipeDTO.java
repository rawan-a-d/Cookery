package service.model.DTO;

import java.util.Objects;

public class RecipeDTO {
    private int id;
    private String name;
    private String image;
    private UserDTO user;
    private int favouriteId;
    private boolean isFavourite;

    public RecipeDTO() {
    }


    public RecipeDTO(int id, UserDTO user) {
        this.id = id;
        this.user = user;
    }

    public RecipeDTO(int id, String name, String image, UserDTO user) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.user = user;
    }

    public RecipeDTO(int id, String name, String image, UserDTO user, int favouriteId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.user = user;
        this.favouriteId = favouriteId;
    }

    public RecipeDTO(int id, String name, String image, UserDTO user, int favouriteId, boolean isFavourite) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.user = user;
        this.favouriteId = favouriteId;
        this.isFavourite = isFavourite;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "RecipeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", user=" + user +
                ", favouriteId=" + favouriteId +
                ", isFavourite=" + isFavourite +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDTO recipeDTO = (RecipeDTO) o;
        return id == recipeDTO.id &&
                favouriteId == recipeDTO.favouriteId &&
                isFavourite == recipeDTO.isFavourite &&
                Objects.equals(name, recipeDTO.name) &&
                Objects.equals(image, recipeDTO.image) &&
                Objects.equals(user, recipeDTO.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, user, favouriteId, isFavourite);
    }
}
