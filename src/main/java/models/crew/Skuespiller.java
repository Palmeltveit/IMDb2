package models.crew;

import DB.ActiveDomainObject;
import models.Episode;
import models.Film;
import models.IFilm;
import models.Person;

import java.sql.Connection;
import java.util.Optional;

/**
 * Class Skuespiller corresponds to actors in both movies and episodes
 */
public class Skuespiller implements ActiveDomainObject {

    private IFilm film;
    private Person person;
    private String rolle;

    /**
     *
     * @param person - Person who is the actor
     * @param film - the movie the person is acting is in
     * @param rolle - the role the person has in the movie
     */
    public Skuespiller(Person person, IFilm film, String rolle) {
        this.film = film;
        this.person = person;
        this.rolle = rolle;
    }

    /**
     * does nothing - never need to retrieve a single Actor
     * @param conn - connection to database
     */
    public void initialize(Connection conn) {
        System.out.println("unødvendig å hente ut en spesifikk skuespiller i en spesifikk film -> ikke implementert");
    }

    /**
     * Really does nothing else than call initialize again, not really necessary
     * @param conn - connection to database
     */
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    /**
     * Saves the actor to database, if not already saved.
     * @param conn - connection to database
     */
    public void save(Connection conn) {
        film.addActor(conn, this);
    }

    /**
     *
     * @return Person person who is acting
     */
    public Person getPerson() {
        return person;
    }

    /**
     *
     * @return String rolle - the role of the actor
     */
    public String getRolle() {
        return rolle;
    }

    /**
     *
     * @return an Optional<Film> - empty if no movie is set
     */
    public Optional<Film> getFilm() {
        if (film instanceof Film) return Optional.of((Film) film);
        return Optional.empty();
    }

    /**
     *
     * @return an Optional<Episode> - empty if no episode is set
     */
    public Optional<Episode> getEpisode() {
        if (film instanceof Episode) return Optional.of((Episode) film);
        return Optional.empty();
    }
}
