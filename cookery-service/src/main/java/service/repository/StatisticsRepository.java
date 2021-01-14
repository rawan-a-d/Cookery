package service.repository;

import service.model.DTO.ChartDataDTO;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsRepository {
    JDBCRepository jdbcRepository;

    public StatisticsRepository() {
        this.jdbcRepository = new JDBCRepository();
    }

    // Favourites per user
    public ChartDataDTO getFavouritesPerUser() throws CookeryDatabaseException {
        String sql = "SELECT user.name AS userName, count(*) AS favouritesNr FROM user " +
                        "INNER JOIN user_favourite_recipe r on user.id = r.user_id " +
                        "GROUP BY user.id";
        ChartDataDTO chartData = new ChartDataDTO("Number of favourites recipes per user");
        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                int favouritesNr = resultSet.getInt("favouritesNr");

                chartData.addX(userName);
                chartData.addY(favouritesNr);
            }

        } catch (SQLException | URISyntaxException throwable) {
            throw new CookeryDatabaseException("Can't read statistics from the database", throwable);
        }

        return chartData;
    }


    // Posted recipes per month
    public ChartDataDTO getPostedRecipesPerMonth() throws CookeryDatabaseException {
        String sql = "SELECT MONTH(DATE) AS month, MONTHNAME(date) AS monthName, COUNT(*) AS nrOfRecipes FROM recipe " +
                        "GROUP BY MONTH(DATE)";

        ChartDataDTO chartData = new ChartDataDTO("Recipes per month");
        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String month = resultSet.getString("monthName");
                int nrOfRecipes = resultSet.getInt("nrOfRecipes");

                chartData.addX(month);
                chartData.addY(nrOfRecipes);
            }

        } catch (SQLException | URISyntaxException throwable) {
            throw new CookeryDatabaseException("Can't read statistics from the database", throwable);
        }

        return chartData;
    }


    // SELECT COUNT(*) AS nrOfFollowers, followee_id FROM follow
    //    GROUP BY followee_id
    //    ORDER BY nrOfFollowers
    //    LIMIT 5;

    //SELECT COUNT(*) AS nrOfFollowers, user.id, user.name FROM follow
    //    LEFT JOIN user on user.id = followee_id
    //    GROUP BY followee_id
    //    ORDER BY nrOfFollowers DESC
    //    LIMIT 5;
    public ChartDataDTO getTopFollowedUsers() throws CookeryDatabaseException {
        String sql = "SELECT COUNT(*) AS nrOfFollowers, user.id, user.name FROM follow " +
                        "LEFT JOIN user on user.id = followee_id " +
                        "GROUP BY followee_id " +
                        "ORDER BY nrOfFollowers DESC " +
                        "LIMIT 5";

        ChartDataDTO chartData = new ChartDataDTO("Top followed users");
        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int nrOfFollowers = resultSet.getInt("nrOfFollowers");

                chartData.addX(name);
                chartData.addY(nrOfFollowers);
            }

        } catch (SQLException | URISyntaxException throwable) {
            throw new CookeryDatabaseException("Can't read statistics from the database", throwable);
        }

        return chartData;
    }
}
