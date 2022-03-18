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
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
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
    public void createCardTable() {
        Connection connection = null;
        String sqlCreateTableStatement = "CREATE TABLE IF NOT EXISTS card (\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "number TEXT NOT NULL\n," +
                "pin TEXT NOT NULL,\n" +
                "balance INTEGER DEFAULT 0)";

        String url = "jdbc:sqlite:/Users/seanrogan/IdeaProjects/Simple Banking System1/Simple Banking System/task/" + fileName;
        try {
            connection = DriverManager.getConnection(url);
            Statement s = connection.createStatement();
            s.execute(sqlCreateTableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void updateBalance (String balance, String cardNumber) {
        String sql = "UPDATE card SET balance = ? WHERE number =" + cardNumber;
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, cardNumber);
            ps.setString(1, balance);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void update(String sql) {
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public ResultSet select(String sql) {
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return null;
    }
//todo this isnt deleting cards
    public void deleteAccount(String cardNumber) {
        String sql = "DELETE FROM card WHERE number IS ?";
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cardNumber);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void insert(String sql) {
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void insertNewAccount(String customerID, String cardNumber, String pin) {
        String sql = "INSERT or REPLACE INTO card(id, number, pin) VALUES(?,?,?)";
        Connection connection = null;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(customerID));
            ps.setString(2, cardNumber);
            ps.setString(3, pin);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public float queryBalance(String cardNumber) {
        String sql = "SELECT balance FROM card WHERE number = ?";
        Connection connection = null;
        float currentBalance = 0.0F;
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                currentBalance = Float.parseFloat(rs.getString("balance"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return currentBalance;
    }

    public boolean findAccount(String cardNumber) {
        Connection connection = null;
            String sql = "SELECT * FROM card";
        try{
            connection = this.connect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString("number").equals(cardNumber)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
            return false;
    }

}

