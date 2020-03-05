import DB.DBConnection;
import models.Bruker;
import models.Film;
import models.Person;
import models.Produksjonsselskap;
import models.reactions.Rating;

import java.sql.Date;

public class Main {

    public static void main(String[] args){
        DBConnection connection = new DBConnection();
        connection.connect();

        Produksjonsselskap produksjonsselskap = new Produksjonsselskap("Pål Productions", new Date(0));
        Film film = new Film(produksjonsselskap, "XXX - Pizza guy getting the job DONE! Hot Stuff!",
                13, 2020, Date.valueOf("2020-01-01"),
                "Pizza guy Pål having a great day at the job!", 1);

        produksjonsselskap.save(connection.getConn());
        film.save(connection.getConn());

        Bruker bruker = new Bruker("P.G", "plåp");
        bruker.save(connection.getConn());
        Rating brukerRating = new Rating(bruker, film, "My favourite ;)", "The pizza guy is awesome, wish there were more!", 10);
        brukerRating.save(connection.getConn());

        System.out.println("brukerID: " + bruker.getID() + " -- ratingID: " + brukerRating.getID());
    }

}
