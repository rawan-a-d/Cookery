package service.model.DTO;

import java.util.Objects;

public class UserFollowDTO {
    private int followId;
    private UserBase user;
    private String image;

    public UserFollowDTO(int followId, UserBase user, String image) {
        this.followId = followId;
        this.user = user;
        this.image = image;
    }

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
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
        return followId == that.followId &&
                Objects.equals(user, that.user) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, user, image);
    }

    @Override
    public String toString() {
        return "UserFollowDTO{" +
                "followId=" + followId +
                ", user=" + user +
                ", image='" + image + '\'' +
                '}';
    }
}
