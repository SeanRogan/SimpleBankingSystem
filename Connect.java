package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Connect to SQLite DB
public class Connect {

    public static void connect(String [] args) {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:sampleDB.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to DB has been established");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            //close connection
            try {
                if(connection != null) connection.close();

            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }



}

