package service.model.DTO;

import service.model.User;

public class RecipeDTO {
    private int id;
    private static int idSeeder = 0;
    private String name;
    private String image;
    private User user;
    private int favouriteId;
    private boolean isFavourite;

    public RecipeDTO() {
    }

    public RecipeDTO(int id, String name, String image, User user, int favouriteId, boolean isFavourite) {
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

    public static int getIdSeeder() {
        return idSeeder;
    }

    public static void setIdSeeder(int idSeeder) {
        RecipeDTO.idSeeder = idSeeder;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
}
