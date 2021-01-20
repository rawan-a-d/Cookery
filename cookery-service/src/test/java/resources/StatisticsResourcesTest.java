package resources;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.model.DTO.ChartDataDTO;
import service.repository.JDBCRepository;
import service.resources.StatisticsResources;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.sql.DriverManager;

import static org.glassfish.jersey.message.internal.ReaderWriter.UTF8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StatisticsResourcesTest extends JerseyTest {
    @Mock
    JDBCRepository jdbcRepository;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        Mockito.lenient().when(jdbcRepository.getDatabaseConnection()).thenReturn(
                DriverManager.getConnection("jdbc:h2:mem:~/test") // test is the name of the folder inside db
        );

        super.setUp();

        Class.forName ("org.h2.Driver");
        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:data.sql", UTF8, false);
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        RunScript.execute("jdbc:h2:mem:~/test", "", "", "classpath:shutdown.sql", UTF8, false);
    }


    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0"); // runs on available port
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(StatisticsResources.class)
                .register(MultiPartFeature.class);
//                .register(AuthenticationFilter.class);
    }


    @Test
    public void getStatistic_favouritesRecipesPerUser() {
        ChartDataDTO expectedChartData = new ChartDataDTO("Number of favourites recipes per user");
        expectedChartData.addX("Rawan");
        expectedChartData.addY(2);
        expectedChartData.addX("Anas");
        expectedChartData.addY(1);

        Response response = target("statistics").queryParam("type", "favourites-recipes-per-user").request().get();

        ChartDataDTO actualChartData = response.readEntity(ChartDataDTO.class);

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedChartData.getTitle(), actualChartData.getTitle());
        assertArrayEquals(expectedChartData.getxAxis().toArray(), actualChartData.getxAxis().toArray());
        assertArrayEquals(expectedChartData.getyAxis().toArray(), actualChartData.getyAxis().toArray());
    }


    @Test
    public void getStatistic_recipesPerMonth() {
        ChartDataDTO expectedChartData = new ChartDataDTO("Recipes per month");
        expectedChartData.addX("February");
        expectedChartData.addY(1);
        expectedChartData.addX("March");
        expectedChartData.addY(1);
        expectedChartData.addX("May");
        expectedChartData.addY(1);
        expectedChartData.addX("July");
        expectedChartData.addY(1);

        Response response = target("statistics").queryParam("type", "recipes-per-month").request().get();

        ChartDataDTO actualChartData = response.readEntity(ChartDataDTO.class);

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedChartData.getTitle(), actualChartData.getTitle());
        assertArrayEquals(expectedChartData.getxAxis().toArray(), actualChartData.getxAxis().toArray());
        assertArrayEquals(expectedChartData.getyAxis().toArray(), actualChartData.getyAxis().toArray());
    }


    @Test
    public void getStatistic_topFollowedUsers() {
        ChartDataDTO expectedChartData = new ChartDataDTO("Top followed users");
        expectedChartData.addX("Rawan");
        expectedChartData.addY(2);
        expectedChartData.addX("Raneem");
        expectedChartData.addY(1);
        expectedChartData.addX("Anas");
        expectedChartData.addY(1);

        Response response = target("statistics")
                            .queryParam("type", "top-followed-users")
                            .request()
                            .get();

        ChartDataDTO actualChartData = response.readEntity(ChartDataDTO.class);

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedChartData.getTitle(), actualChartData.getTitle());
        assertArrayEquals(expectedChartData.getxAxis().toArray(), actualChartData.getxAxis().toArray());
        assertArrayEquals(expectedChartData.getyAxis().toArray(), actualChartData.getyAxis().toArray());
    }
}
