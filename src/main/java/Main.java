import DB.DBConnection;
import models.Bruker;

import java.sql.*;

public class Main {
    private static String databaseURL = null;
    private static String databaseUsername = null;
    private static String databasePassword = null;

    private static void parseArgs(String[] args) {
        if (args.length < 3) {
            System.err.printf("Usage: java -jar IMDb2.jar <databaseurl> <username> <password>\n");
            System.exit(1);
        }
        databaseURL = args[0];
        databaseUsername = args[1];
        databasePassword = args[2];
    }

    public static void main(String[] args){

        parseArgs(args);

        DBConnection connection = new DBConnection(databaseURL, databaseUsername, databasePassword);
        connection.connect();

        // Bruker b = new Bruker("admin", "admin");
        // b.save(connection.getConn());

        TextUserInterface tui = new TextUserInterface(connection);
        tui.showMenu();
        // tui.insertNewMovie();

        try {
            connection.getConn().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
