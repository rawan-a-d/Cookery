package service.model.DTO;

import service.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationDTO {
    private List<Notification> notifications;
    private int newNotificationsNr;

    public NotificationDTO() {
    }

    public NotificationDTO(int newNotificationsNr) {
        this.newNotificationsNr = newNotificationsNr;
        this.notifications = new ArrayList<>();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public int getNewNotificationsNr() {
        return newNotificationsNr;
    }

    public void setNewNotificationsNr(int newNotificationsNr) {
        this.newNotificationsNr = newNotificationsNr;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "notifications=" + notifications +
                ", newNotificationsNr=" + newNotificationsNr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationDTO that = (NotificationDTO) o;
        return newNotificationsNr == that.newNotificationsNr &&
                Objects.equals(notifications, that.notifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notifications, newNotificationsNr);
    }
}
