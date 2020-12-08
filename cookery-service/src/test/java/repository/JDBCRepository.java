package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCRepository {
    public static boolean dbCreated = false;

    public static void generateData() {
        Connection conn = null;
        Statement stmt = null;
        String sql;
        try {
            //STEP 1: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:h2:mem:~/test");

//            if(!dbCreated) {
                //STEP 2: Execute a query
                System.out.println("Creating table in given database...");
                stmt = conn.createStatement();
                sql = "CREATE TABLE user " +
                        "(id INTEGER not NULL AUTO_INCREMENT, " +
                        " name VARCHAR(255), " +
                        " email VARCHAR(255), " +
                        " password VARCHAR(255)," +
                        " role VARCHAR(255), " +
                        " PRIMARY KEY ( id ))";
                stmt.executeUpdate(sql);

                System.out.println("Created table in given database...");
                sql = "CREATE TABLE recipe " +
                        "(id INTEGER not NULL AUTO_INCREMENT, " +
                        " name VARCHAR(255), " +
                        " description VARCHAR(255), " +
                        " image VARCHAR(255)," +
                        " user_id INTEGER, " +
                        " PRIMARY KEY ( id ), " +
                        " FOREIGN KEY (user_id) REFERENCES user( id )) "; // FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
                stmt.executeUpdate(sql);

                System.out.println("Created table in given database...");
                sql = "CREATE TABLE ingredient " +
                        "(id INTEGER not NULL AUTO_INCREMENT, " +
                        " ingredient VARCHAR(255), " +
                        " amount INTEGER, " +
                        " recipe_id INTEGER, " +
                        " PRIMARY KEY ( id ), " +
                        " FOREIGN KEY (recipe_id) REFERENCES recipe( id )) "; // FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
                stmt.executeUpdate(sql);

                sql = "CREATE TABLE user_favourite_recipe " +
                        "(id INTEGER not NULL AUTO_INCREMENT, " +
                        " user_id INTEGER, " +
                        " recipe_id INTEGER, " +
                        " PRIMARY KEY ( id ), " +
                        " FOREIGN KEY (user_id) REFERENCES user( id ), " +
                        " FOREIGN KEY (recipe_id) REFERENCES recipe( id )) ";
                stmt.executeUpdate(sql);

//                dbCreated = true;

//            }


                sql = "INSERT INTO user (name, email, password, role) VALUES ('Rawan', 'rawan@gmail.com', '1234', 'admin')";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO user (name, email, password, role) VALUES ('Anas', 'anas@gmail.com', '1234', 'user')";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO user (name, email, password, role) VALUES ('Omar', 'omar@gmail.com', '1234', 'admin')";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO user (name, email, password, role) VALUES ('Raneem', 'raneem@gmail.com', '1234', 'user')";
                stmt.executeUpdate(sql);


                sql = "INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 1', 'recipe 1 desc', 'recipe 1 image', 1)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 2', 'recipe 2 desc', 'recipe 2 image', 1)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 3', 'recipe 3 desc', 'recipe 3 image', 2)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 4', 'recipe 4 desc', 'recipe 4 image', 3)";
                stmt.executeUpdate(sql);


                sql = "INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 2, 1)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('garlic', 1, 1)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('tomato', 2, 2)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 5, 4)";
                stmt.executeUpdate(sql);

                sql = "INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 2)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 3)";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (2, 1)";
                stmt.executeUpdate(sql);



                // STEP 3: Clean-up environment
                stmt.close();
                conn.close();
//            }

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
