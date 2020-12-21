package service.controller;

import service.model.DTO.ChartDataDTO;
import service.repository.CookeryDatabaseException;
import service.repository.StatisticsRepository;

import java.util.logging.Logger;

public class StatisticsController {
    private final static Logger LOGGER = Logger.getLogger(UsersController.class.getName());
    private StatisticsRepository statisticsRepository;

    public StatisticsController () {
        this.statisticsRepository = new StatisticsRepository();
    }

    public ChartDataDTO getFavouritesPerUser() {
        try {
            return statisticsRepository.getFavouritesPerUser();
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

            return null;
        }
    }


    public ChartDataDTO getPostedRecipesPerMonth() {
        try {
            return statisticsRepository.getPostedRecipesPerMonth();
        }
        catch (CookeryDatabaseException ex) {
            LOGGER.info(ex.getMessage()); // Compliant

            return null;
        }
    }
}
