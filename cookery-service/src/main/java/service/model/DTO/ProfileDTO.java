package service.model.DTO;

public class ProfileDTO {
    private UserBase user;
    private int recipesNr;
    private int followersNr;

    public ProfileDTO(UserBase user, int recipesNr, int followersNr) {
        this.user = user;
        this.recipesNr = recipesNr;
        this.followersNr = followersNr;
    }

    public UserBase getUser() {
        return user;
    }

    public void setUser(UserBase user) {
        this.user = user;
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
