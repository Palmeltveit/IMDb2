import DB.DBConnection;
import models.*;
import models.crew.CrewMember;
import models.crew.CrewTypes;
import models.crew.Skuespiller;
import models.reactions.Rating;
import models.FilmLagetFor;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void resetDB(Connection conn) {
        try (
                PreparedStatement deletePersons = conn.prepareStatement("DELETE FROM Person");
                PreparedStatement deleteFilmActors = conn.prepareStatement("DELETE FROM FilmSkuespiller");
                PreparedStatement deleteEpisodeActors = conn.prepareStatement("DELETE FROM EpisodeSkuespiller");
        ) {
            assert deleteEpisodeActors.execute();
            assert deleteFilmActors.execute();
            assert deletePersons.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Serie serie = new Serie(produksjonsselskap, "Adventures of Pål the Pizza Guy",
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

        Person brad = new Person("Brad Pitt", "USA", 1963);
        brad.save(connection.getConn());
        System.out.println("Brad Pitt: " + brad.getNavn());

        CrewMember member = new CrewMember(brad, episode, CrewTypes.REGISSOR);
        member.save(connection.getConn());

        Skuespiller actor = new Skuespiller(brad, episode, "guy in background");
        actor.save(connection.getConn());

        Film deadpool = new Film(produksjonsselskap, "Deadpool", 190, 2018,
                Date.valueOf("2019-01-02"), "En grei nok film", FilmLagetFor.KINO.ord());
        Skuespiller bradInDeadpool = new Skuespiller(brad, deadpool, "invisible guy");
        bradInDeadpool.save(connection.getConn());

        // System.out.println("brukerID: " + bruker.getID() +
        //         " -- filmRatingID: " + brukerFilmRating.getID() +
        //         " -- serieRatingID: " + brukerSerieRating.getID() +
        //         " -- episodeRatingID: " + brukerEpisodeRating.getID());

        System.out.println(bradInDeadpool.getRolle());
        System.out.println(bradInDeadpool.getPerson().getID());

        brad.findAllActorRoles(connection.getConn()).stream().forEach(s -> System.out.println(s.getRolle()));

        Kategori thriller = new Kategori("nyting", "enda en beskrivelse");
        thriller.save(connection.getConn());

        Kategori anotherOne = new Kategori("beats", "the dust");
        anotherOne.save(connection.getConn());

        deadpool.addCategory(connection.getConn(), thriller);
        deadpool.addCategory(connection.getConn(), anotherOne);
        deadpool.refresh(connection.getConn());


        Kategori kategori = new Kategori(1);
        kategori.initialize(connection.getConn());
        System.out.print(kategori.getNavn());
        System.out.print(kategori.getBeskrivelse());

        Film f = new Film(deadpool.getID());
        f.initialize(connection.getConn());
        List<Kategori> kategorier = f.getKategorier();
        kategorier.stream().forEach(k -> System.out.println("KATEGORI: " + k.getID() + ": " + k.getNavn() + " " + k.getBeskrivelse()));

        Kategori k = kategorier.get(0);
        List<Film> nytingFilms = k.findAllFilmsByCategory(connection.getConn());
        nytingFilms.forEach(newfilm -> System.out.println(newfilm.getTittel() + " " + newfilm.getProduksjonsselskap().getNavn()));

        List<Kategori> allCategories = Kategori.findAllCategories(connection.getConn());
        allCategories.forEach(c -> System.out.println(c.getNavn()));

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
