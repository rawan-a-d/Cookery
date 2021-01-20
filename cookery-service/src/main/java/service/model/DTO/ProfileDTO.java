package service.model.DTO;

import java.util.Objects;

public class ProfileDTO {
    private UserBase user;
    private String image;
    private int recipesNr;
    private int followersNr;
    private int followeesNr;

    public ProfileDTO(UserBase user, String image, int recipesNr, int followersNr, int followeesNr) {
        this.user = user;
        this.image = image;
        this.recipesNr = recipesNr;
        this.followersNr = followersNr;
        this.followeesNr = followeesNr;
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

    public int getFolloweesNr() {
        return followeesNr;
    }

    public void setFolloweesNr(int followeesNr) {
        this.followeesNr = followeesNr;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "user=" + user +
                ", image='" + image + '\'' +
                ", recipesNr=" + recipesNr +
                ", followersNr=" + followersNr +
                ", followeesNr=" + followeesNr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO that = (ProfileDTO) o;
        return recipesNr == that.recipesNr &&
                followersNr == that.followersNr &&
                followeesNr == that.followeesNr &&
                Objects.equals(user, that.user) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, image, recipesNr, followersNr, followeesNr);
    }
}
