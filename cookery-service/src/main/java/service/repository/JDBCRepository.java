package service.repository;

import service.controller.RecipesController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class JDBCRepository {
    private final static Logger LOGGER = Logger.getLogger(RecipesController.class.getName());

    // db setup
    // protected
    public Connection getDatabaseConnection() throws URISyntaxException {
        // Get configuration
        URL res = getClass().getClassLoader().getResource("app.properties");
        File configFile = Paths.get(res.toURI()).toFile();

        String url = "";
        String username = "";
        String pass = "";
        Connection connection = null;

        try(FileReader reader = new FileReader(configFile)) {
            Properties properties = new Properties();
            properties.load(reader);

            url = properties.getProperty("host");
            username = properties.getProperty("username");
            pass = properties.getProperty("pass");

            connection = DriverManager.getConnection(url, username, pass);

            connection.setAutoCommit(false);
        }
        catch (SQLException ex) {
            throw new IllegalStateException("JDBC driver failed to connect to the database " + url + " " + username + " " + pass + ".", ex);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        catch (IOException ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }

        return connection;
    }

}
