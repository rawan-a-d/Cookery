package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCRepository {
    public static void generateData() {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 1: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:h2:mem:~/test");

            //STEP 2: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE user " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " name VARCHAR(255), " +
                    " email VARCHAR(255), " +
                    " password VARCHAR(255)," +
                    " role VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            sql = "INSERT INTO user (name, email, password, role) VALUES ('Rawan', 'rawan@gmail.com', '1234', 'admin')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO user (name, email, password, role) VALUES ('Anas', 'anas@gmail.com', '1234', 'user')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO user (name, email, password, role) VALUES ('Omar', 'omar@gmail.com', '1234', 'admin')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO user (name, email, password, role) VALUES ('Raneem', 'raneem@gmail.com', '1234', 'user')";
            stmt.executeUpdate(sql);


            // STEP 3: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        }
    }
}
