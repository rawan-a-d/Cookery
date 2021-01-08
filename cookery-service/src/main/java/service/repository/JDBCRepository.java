package service.repository;

import org.glassfish.jersey.jaxb.internal.XmlRootElementJaxbProvider.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;


public class JDBCRepository {
    private final static Logger LOGGER = Logger.getLogger(JDBCRepository.class.getName());

    // db setup
    public Connection getDatabaseConnection() throws URISyntaxException {
        Properties prop = new Properties();
        String url = "";
        String username = "";
        String pass = "";
        Connection connection = null;

        try {
            prop.load(App.class.getClassLoader().getResourceAsStream("app.properties"));

            url = prop.getProperty("host");
            username = prop.getProperty("username");
            pass = prop.getProperty("pass");

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

//
//        public Connection getDatabaseConnection() throws URISyntaxException {
//        System.out.println("Get db conn ");
//        // Get configuration
//        URL res = getClass().getClassLoader().getResource("app.properties");
//        System.out.println("Res " + res);
//        File configFile = Paths.get(res.toURI()).toFile();
//
//        System.out.println("Config file " + configFile);
//        String url = "";
//        String username = "";
//        String pass = "";
//        Connection connection = null;
//
//        try(FileReader reader = new FileReader(configFile)) {
//            System.out.println("Reader");
//            Class.forName("org.h2.Driver");
//
//            Properties properties = new Properties();
//            properties.load(reader);
//
//            url = properties.getProperty("host");
//            username = properties.getProperty("username");
//            pass = properties.getProperty("pass");
//            System.out.println(url);
//            System.out.println(username);
//            System.out.println(pass);
//
//            connection = DriverManager.getConnection(url, username, pass);
//
//            connection.setAutoCommit(false);
//        }
//        catch (SQLException ex) {
//            throw new IllegalStateException("JDBC driver failed to connect to the database " + url + " " + username + " " + pass + ".", ex);
//        }
//        catch (FileNotFoundException ex) {
//            LOGGER.info(ex.getMessage()); // Compliant
//        }
//        catch (IOException ex) {
//            LOGGER.info(ex.getMessage()); // Compliant
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return connection;
//    }
}
