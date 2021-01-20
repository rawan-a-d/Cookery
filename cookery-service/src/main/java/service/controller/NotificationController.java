package service.controller;

import service.model.DTO.NotificationDTO;
import service.model.DTO.RecipeDTO;
import service.repository.CookeryDatabaseException;
import service.repository.NotificationRepository;

import java.util.logging.Logger;

public class NotificationController {
    private final static Logger LOGGER = Logger.getLogger(RecipesController.class.getName());
    NotificationRepository notificationRepository = new NotificationRepository();

    public NotificationDTO getNotifications(int userId) {
        NotificationDTO notificationDTO = null;
        try {
            notificationDTO = notificationRepository.getNotifications(userId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return notificationDTO;
    }

    public boolean createNotification(RecipeDTO recipe) {
        boolean result = false;
        try {
            result = notificationRepository.createNotification(recipe);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }

    public boolean markAsSeen(int userId) {
        boolean result = false;
        try {
            result = notificationRepository.markAsSeen(userId);
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return result;
    }
}
