package service.repository;

import service.controller.RecipesController;
import service.model.DTO.NotificationDTO;
import service.model.DTO.RecipeDTO;
import service.model.DTO.UserFollowDTO;
import service.model.Notification;

import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class NotificationRepository {
    private final static Logger LOGGER = Logger.getLogger(RecipesController.class.getName());

    JDBCRepository jdbcRepository;

    public NotificationRepository() {
        this.jdbcRepository = new JDBCRepository();
    }

    public NotificationDTO getNotifications(int userId) throws CookeryDatabaseException {
        NotificationDTO notificationDTO = null;
        System.out.println("GET NOTIFICATIONS");

        // SELECT n.*, un.is_seen, (SELECT COUNT(*) FROM user_notification WHERE user_notification.is_seen = false) AS newNotificationsNr FROM notification AS n
        //                                                                                                                                                                           INNER JOIN user_notification AS un ON un.notification_id = n.id AND un.user_id = 88
        String  sql = "SELECT n.*, un.is_seen, (SELECT COUNT(*) FROM user_notification WHERE user_notification.user_id = ? AND user_notification.is_seen = false) AS new_notifications_nr FROM notification AS n " +
                "LEFT JOIN user_notification AS un ON un.notification_id = n.id AND un.user_id = ? " +
                "ORDER BY n.id DESC";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            System.out.println(statement);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("execute");

            //  public Notification(int id, String userName, String recipeName, int recipeId, LocalDateTime dateTime) {
            while(resultSet.next()) {
                System.out.println("While");
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("user_name");
                String recipeName = resultSet.getString("recipe_name");
                int recipeId = resultSet.getInt("recipe_id");
                LocalDateTime dateTime = resultSet.getTimestamp(5).toLocalDateTime();
                boolean isSeen = resultSet.getBoolean("is_seen");
                int newNotificationsNr = resultSet.getInt("new_notifications_nr");

                if(notificationDTO == null) {
                    System.out.println("NOTIFICATION FIRST TIME");
                    notificationDTO = new NotificationDTO(newNotificationsNr);
                }

                System.out.println("id " + id);
                System.out.println("userName " + userName);
                System.out.println("recipeName " + recipeName);
                System.out.println("recipeId " + recipeId);
                System.out.println("dateTime " + dateTime);

                System.out.println("newNotificationsNr " + newNotificationsNr);

                Notification notification = new Notification(id, userName, recipeName, recipeId, dateTime, isSeen);

                System.out.println("Notification");
                System.out.println(notificationDTO);
                notificationDTO.addNotification(notification);
            }

            connection.commit();
        }
        catch (SQLException | URISyntaxException throwable) {
            throw new CookeryDatabaseException("Cannot read notifications from the database.", throwable);
        }
        return notificationDTO;
    }


    public boolean createNotification(RecipeDTO recipe) throws CookeryDatabaseException {
        System.out.println("Create notification ");
        String sql = "INSERT INTO notification (user_name, recipe_id, recipe_name, date) VALUES (?, ?, ?, ?)";


        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementUserNotification = connection.prepareStatement("INSERT INTO user_notification (user_id, notification_id) VALUES (?, ?)");
        ) {
            preparedStatement.setString(1, recipe.getUser().getName());
            preparedStatement.setInt(2, recipe.getId());
            preparedStatement.setString(3, recipe.getName());
            preparedStatement.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            System.out.println(preparedStatement);
//            System.out.println("DATE " + date);
            if(resultSet.next()) {
                int notificationId = resultSet.getInt(1);

                // get Followers
                UsersRepository usersRepository = new UsersRepository();
                List<UserFollowDTO> followers = usersRepository.getFollowers(recipe.getUser().getId());

                System.out.println("Followers " + followers);
                for(UserFollowDTO follower: followers) {
                    preparedStatementUserNotification.setInt(1, follower.getUser().getId());
                    preparedStatementUserNotification.setInt(2, notificationId);

                    System.out.println(preparedStatementUserNotification);
                    preparedStatementUserNotification.addBatch();
                    preparedStatementUserNotification.clearParameters();
                }

                preparedStatementUserNotification.executeBatch();

                connection.commit();

                return true;
            }
            else {
                throw new CookeryDatabaseException("Cannot get the id of the new notification.");
            }

        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot create new notification.", throwable);
        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return false;
    }


    public boolean markAsSeen(int userId) throws CookeryDatabaseException {
        System.out.println("Create notification ");
        String sql = "UPDATE user_notification " +
                "SET is_seen = true WHERE user_id = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, userId);

            int affectedRow = preparedStatement.executeUpdate();

            connection.commit();

            return true;
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot mark new notification as seen.", throwable);
        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return false;
    }
}
