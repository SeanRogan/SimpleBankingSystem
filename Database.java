package banking;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
//Connect to SQLite DB
public class Database {
    private String fileName;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    Database(String[] args) {
        this.fileName = args[1];
        createNewDatabase(fileName);

    }


    private static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:/Users/seanrogan/IdeaProjects/Simple Banking System1/Simple Banking System/task/" + fileName;
        try{
            Connection connect = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void createTable() {

    }
    public void update() {}
    public void delete() {}
    public void insert() {}
    public void select() {}

}

