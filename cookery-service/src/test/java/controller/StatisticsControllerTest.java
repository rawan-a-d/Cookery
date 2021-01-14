package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.controller.StatisticsController;
import service.model.DTO.ChartDataDTO;
import service.repository.CookeryDatabaseException;
import service.repository.StatisticsRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {
    @InjectMocks
    StatisticsController statisticsController;

    @Mock
    StatisticsRepository statisticsRepository;


    @Test
    public void getFavouritesPerUser() throws CookeryDatabaseException {
            ChartDataDTO expectedChartData = new ChartDataDTO("Number of favourites recipes per user");
            expectedChartData.addX("Rawan");
            expectedChartData.addY(2);
            expectedChartData.addX("Anas");
            expectedChartData.addY(1);

            when(statisticsRepository.getFavouritesPerUser()).thenReturn(
                    expectedChartData
            );

            ChartDataDTO actualChartData = statisticsController.getFavouritesPerUser();

            assertEquals(expectedChartData, actualChartData);
    }


    @Test
    public void getPostedRecipesPerMonth() throws CookeryDatabaseException {
        ChartDataDTO expectedChartData = new ChartDataDTO("Recipes per month");
        expectedChartData.addX("February");
        expectedChartData.addY(1);
        expectedChartData.addX("March");
        expectedChartData.addY(1);
        expectedChartData.addX("May");
        expectedChartData.addY(1);
        expectedChartData.addX("July");
        expectedChartData.addY(1);

        when(statisticsRepository.getPostedRecipesPerMonth()).thenReturn(
                expectedChartData
        );

        ChartDataDTO actualChartData = statisticsController.getPostedRecipesPerMonth();

        assertEquals(expectedChartData, actualChartData);
    }


    @Test
    public void getTopFollowedUsers() throws CookeryDatabaseException {
        ChartDataDTO expectedChartData = new ChartDataDTO("Top followed users");
        expectedChartData.addX("Rawan");
        expectedChartData.addY(2);
        expectedChartData.addX("Raneem");
        expectedChartData.addY(1);
        expectedChartData.addX("Anas");
        expectedChartData.addY(1);

        when(statisticsRepository.getTopFollowedUsers()).thenReturn(
                expectedChartData
        );

        ChartDataDTO actualChartData = statisticsController.getTopFollowedUsers();

        assertEquals(expectedChartData, actualChartData);
    }
}