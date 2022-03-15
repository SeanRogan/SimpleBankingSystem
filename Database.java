package banking;


import org.sqlite.jdbc4.JDBC4Statement;

import java.sql.*;

//Connect to SQLite DB
public class Database {
    private String fileName;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //constructor takes db file name from command line
    // args and creates new db file
    Database(String[] args)  {
        this.fileName = args[1];
        createNewDatabase(fileName);

    }

    private void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:/Users/seanrogan/IdeaProjects/Simple Banking System1/Simple Banking System/task/" + fileName;
        setUrl(url);
        Connection connect = null;
        try{
            connect = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if(connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());                }
            }
        }
    }

    public Connection connect() {
        String url = getUrl();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
    public void createCardTable() throws SQLException {
        Connection connect = null;
        String sqlCreateTableStatement = "CREATE TABLE IF NOT EXISTS card (\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "number TEXT NOT NULL\n," +
                "pin TEXT NOT NULL,\n" +
                "balance INTEGER DEFAULT 0)";

        String url = "jdbc:sqlite:/Users/seanrogan/IdeaProjects/Simple Banking System1/Simple Banking System/task/" + fileName;
        try {

            connect = DriverManager.getConnection(url);
            Statement s = connect.createStatement();
            s.execute(sqlCreateTableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if(connect != null) {
                connect.close();
            }
        }
    }
    public void update() {}
    public void delete() {}

    public void insert(String sql) {
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void insertNewAccount(String customerID, String cardNumber, String pin) {
        String sql = "INSERT or REPLACE INTO card(id, number, pin) VALUES(?,?,?)";
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(customerID));
            ps.setString(2, cardNumber);
            ps.setString(3, pin);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void select() {}

}

