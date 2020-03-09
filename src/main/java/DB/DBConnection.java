package DB;

import java.sql.*;
import java.util.Properties;

public class DBConnection {
    protected Connection conn;

    private String url, username, password;

    public DBConnection (String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // using MySQL 8.0.

            // Properties for user and password.
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);
            conn = DriverManager.getConnection(url, p);

        } catch (Exception e) {
            throw new RuntimeException("Unable to connect", e);
        }
    }

    public Connection getConn() {
        return conn;
    }
}