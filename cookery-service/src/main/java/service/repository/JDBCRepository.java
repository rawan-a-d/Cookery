package service.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCRepository {

    // db setup
    protected Connection getDatabaseConnection() throws CookeryDatabaseException {
//        String url = "jdbc:mysql://studmysql01.fhict.local:3306/dbi407847";
        String url = "jdbc:mysql://localhost:3306/schema_cookery";
        String username = "root";
        String password = "";

//        String username = "dbi407847";
//        String password = "dbi407847";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            connection.setAutoCommit(false);

            return connection;
        }
        catch (SQLException ex) {
            throw new IllegalStateException("JDBC driver failed to connect to the database " + url + ".", ex);
        }
    }
}
