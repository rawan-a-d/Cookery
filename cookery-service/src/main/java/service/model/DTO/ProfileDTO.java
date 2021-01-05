package service.model.DTO;

public class ProfileDTO {
    private UserBase user;
    private String image;
    private int recipesNr;
    private int followersNr;

    public ProfileDTO(UserBase user, String image, int recipesNr, int followersNr) {
        this.user = user;
        this.image = image;
        this.recipesNr = recipesNr;
        this.followersNr = followersNr;
    }

    public UserBase getUser() {
        return user;
    }

    public void setUser(UserBase user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRecipesNr() {
        return recipesNr;
    }

    public void setRecipesNr(int recipesNr) {
        this.recipesNr = recipesNr;
    }

    public int getFollowersNr() {
        return followersNr;
    }

    public void setFollowersNr(int followersNr) {
        this.followersNr = followersNr;
    }
}
