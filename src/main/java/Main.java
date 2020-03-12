import DB.DBConnection;
import models.Bruker;
import models.Film;
import models.Produksjonsselskap;

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
        DBConnection connection = new DBConnection();
        connection.connect();

        System.out.println( "ALKSDJLAKJSD" );

        Produksjonsselskap produksjonsselskap = new Produksjonsselskap("Pål Productions", new Date(0));
        Film film = new Film(produksjonsselskap, "XXX - Pizza guy getting the job DONE! Hot Stuff!",
                13, 2020, Date.valueOf("2020-01-01"),
                "Pizza guy Pål having a great day at the job!", 1);

        produksjonsselskap.save(connection.getConn());
        film.save(connection.getConn());

        Serie serie = new Serie(produksjonsselskap, "Plapp",
                40, 2020, new Date(0),
                "More of Pål the Pizza Guy every week!", 2);
        serie.save(connection.getConn());

        Episode episode = new Episode(serie, 1, 1);
        episode.save(connection.getConn());

        // Bruker bruker = new Bruker("root", "toor");
        // bruker.save(connection.getConn());

        // Bruker bruker = new Bruker("Pauli Gg", "plåp");
        // bruker.save(connection.getConn());

        // Rating brukerFilmRating = new Rating(bruker, film, "My favourite ;)",
        //          "The pizza guy is awesome, wish there were more!", 10);
        //  brukerFilmRating.save(connection.getConn());

        //  Rating brukerSerieRating = new Rating(bruker, serie, "Even better than the movie!",
        //          "Love it", 10);
        //  brukerSerieRating.save(connection.getConn());

        // Rating brukerEpisodeRating = new Rating(bruker, episode, "Even episode so far!",
        //         "enjoyed every minute", 10);
        // brukerEpisodeRating.save(connection.getConn());

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
