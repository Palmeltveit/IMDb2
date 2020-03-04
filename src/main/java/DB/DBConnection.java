package DB;

import java.sql.*;
import java.util.Properties;

public class DBConnection {
    protected Connection conn;

    public DBConnection () {}
    
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // using MySQL 8.0.

            // Properties for user and password.
            Properties p = new Properties();
            p.put("user", "BigP");
            p.put("password", "PgiB");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/imdb2?serverTimezone=UTC&autoReconnect=true&useSSL=false",
                    p);

        } catch (Exception e) {
            throw new RuntimeException("Unable to connect", e);
        }
    }

    public static long executeAndCheckInsertWithReturnId(PreparedStatement statement) throws Exception{
        executeAndCheckInsert(statement);
        ResultSet generatedKeysSet = statement.getGeneratedKeys();
        if(generatedKeysSet.next()){
            return generatedKeysSet.getLong(1);
        } else {
            throw new RuntimeException("failed to retrieve id, RETURN_GENERATED_KEYS might not be set.");
        }
    }

    public static void executeAndCheckInsert(PreparedStatement statement) throws SQLException{
        int insertedRows = statement.executeUpdate();
        if(insertedRows == 0){
            throw new SQLException("failed to insert new Film row");
        }
    }

    public Connection getConn() {
        return conn;
    }
}