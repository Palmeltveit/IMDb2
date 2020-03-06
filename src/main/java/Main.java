import DB.DBConnection;
import models.*;
import models.crew.CrewMember;
import models.crew.CrewTypes;
import models.crew.Skuespiller;
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

        Serie serie = new Serie(produksjonsselskap, "Adventures of Pål the Pizza Guy", 40, 2020, new Date(0),
                "More of Pål the Pizza Guy every week!", 2);
        serie.save(connection.getConn());

        Episode episode = new Episode(serie, 1, 1);
        episode.save(connection.getConn());

        Bruker bruker = new Bruker("Pauli P", "plåp");
        bruker.save(connection.getConn());

        Rating brukerFilmRating = new Rating(bruker, film, "My favourite ;)", "The pizza guy is awesome, wish there were more!", 10);
        brukerFilmRating.save(connection.getConn());

        Rating brukerSerieRating = new Rating(bruker, serie, "Even better than the movie!", "Love it", 10);
        brukerSerieRating.save(connection.getConn());

        Rating brukerEpisodeRating = new Rating(bruker, episode, "Even episode so far!", "enjoyed every minute", 10);
        brukerEpisodeRating.save(connection.getConn());

        Person person = new Person("Brad Pitt", "USA", 1963);
        person.save(connection.getConn());

        CrewMember member = new CrewMember(person, episode, CrewTypes.REGISSOR);
        member.save(connection.getConn());

        Skuespiller actor = new Skuespiller(person, episode, "guy in background");
        actor.save(connection.getConn());

        System.out.println("brukerID: " + bruker.getID() +
                " -- filmRatingID: " + brukerFilmRating.getID() +
                " -- serieRatingID: " + brukerSerieRating.getID() +
                " -- episodeRatingID: " + brukerEpisodeRating.getID()
        );
    }

}
