package service.model;

import java.time.LocalDateTime;
import java.util.Objects;

// notification table
// id
// user-name
// recipe-name
// recipe-id
// date
public class Notification {
    private int id;
    private String userName;
    private String recipeName;
    private int recipeId;
    private LocalDateTime dateTime;
    private boolean isSeen;

    public Notification() {
    }

    public Notification(String userName, String recipeName, int recipeId) {
        this.userName = userName;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.dateTime = LocalDateTime.now();
    }

    public Notification(int id, String userName, String recipeName, int recipeId, LocalDateTime dateTime, boolean isSeen) {
        this.id = id;
        this.userName = userName;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.dateTime = dateTime;
        this.isSeen = isSeen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean seen) {
        isSeen = seen;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", recipeId=" + recipeId +
                ", dateTime=" + dateTime +
                ", isSeen=" + isSeen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id &&
                recipeId == that.recipeId &&
                isSeen == that.isSeen &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(recipeName, that.recipeName) &&
                Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, recipeName, recipeId, dateTime, isSeen);
    }
}
