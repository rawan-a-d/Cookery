package service.model.DTO;

import java.util.Objects;

public class UserFollowDTO {
    private UserBase user;
    private String image;

    public UserFollowDTO(UserBase user, String image) {
        this.user = user;
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFollowDTO that = (UserFollowDTO) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, image);
    }

    @Override
    public String toString() {
        return "UserFollowDTO{" +
                "user=" + user +
                ", image='" + image + '\'' +
                '}';
    }
}
