package repository;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.model.DTO.ChartDataDTO;
import service.repository.CookeryDatabaseException;
import service.repository.JDBCRepository;
import service.repository.StatisticsRepository;

import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsRepositoryTest {
    @Mock
    JDBCRepository jdbcRepository;

    @InjectMocks
    StatisticsRepository statisticsRepository;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, URISyntaxException {
        Class.forName ("org.h2.Driver");

        when(jdbcRepository.getDatabaseConnection()).thenReturn(
                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
        );

        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:data.sql", UTF8, false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:shutdown.sql", UTF8, false);
    }


    @Test
    public void getFavouritesPerUser() throws CookeryDatabaseException {
        ChartDataDTO expectedChartData = new ChartDataDTO("Number of favourites recipes per user");
        expectedChartData.addX("Rawan");
        expectedChartData.addY(2);
        expectedChartData.addX("Anas");
        expectedChartData.addY(1);

        ChartDataDTO actualChartData = statisticsRepository.getFavouritesPerUser();

        assertEquals(expectedChartData, actualChartData);
    }


    @Test
    public void getPostedRecipesPerMonth() throws CookeryDatabaseException { // ***********************
        ChartDataDTO expectedChartData = new ChartDataDTO("Recipes per month");
        expectedChartData.addX("February");
        expectedChartData.addY(1);
        expectedChartData.addX("March");
        expectedChartData.addY(1);
        expectedChartData.addX("May");
        expectedChartData.addY(1);
        expectedChartData.addX("July");
        expectedChartData.addY(1);

        ChartDataDTO actualChartData = statisticsRepository.getPostedRecipesPerMonth();

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

        ChartDataDTO actualChartData = statisticsRepository.getTopFollowedUsers();

        assertEquals(expectedChartData, actualChartData);
    }
}
