package DB;

import java.sql.*;
import java.util.Properties;

public class DBConnection {
    protected Connection conn;

    public DBConnection () {}
    
    public void connect() {
        try {
            /*Class.forName("com.mysql.cj.jdbc.Driver"); // using MySQL 8.0.

            // Properties for user and password.
            Properties p = new Properties();
            p.put("user", "root");
            p.put("password", "example");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/imdb2?serverTimezone=UTC&autoReconnect=true&useSSL=false",
                    p);*/

            Class.forName("com.mysql.cj.jdbc.Driver"); // using MySQL 8.0.

            // Properties for user and password.
            Properties p = new Properties();
            p.put("user", "root");
            p.put("password", "example");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/imdb2?serverTimezone=UTC&autoReconnect=true&useSSL=false",
                    p);

        } catch (Exception e) {
            throw new RuntimeException("Unable to connect", e);
        }
    }

    public Connection getConn() {
        return conn;
    }
}