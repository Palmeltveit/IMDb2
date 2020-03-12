package models.reactions;

import DB.ActiveDomainObject;
import models.Bruker;
import models.IFilm;
import models.IRateable;

import java.sql.Connection;

/**
 * A Rating is a rating on a rateable class (movie, series or episode)
 */
public class Rating implements ActiveDomainObject {

    private long ID;
    private Bruker bruker;
    private IRateable rateable;

    private String tittel, innhold;
    private int rating;

    /**
     *
     * @param ID - id of rating in db
     */
    public Rating(int ID) {
        this.ID = ID;
    }

    /**
     *
     * @param bruker - user who is rating
     * @param rateable - the rateable instance (movie, episode, series)
     * @param tittel - title of rating
     * @param innhold - rating  body / comment
     * @param rating - rating number (1-10)
     */
    public Rating(Bruker bruker, IRateable rateable, String tittel, String innhold, int rating) {
        this.bruker = bruker;
        this.rateable = rateable;
        this.tittel = tittel;
        this.innhold = innhold;
        this.rating = rating;
    }

    /**
     * Sets the is after insetion
     * @param ID - id in db
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    /**
     *
     * @return Bruker bruker - user who made rating
     */
    public Bruker getBruker() {
        return bruker;
    }

    /**
     *
     * @return the rateable being rated
     */
    public IRateable getRateable() {
        return rateable;
    }

    public String getTittel() {
        return tittel;
    }

    public String getInnhold() {
        return innhold;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public void initialize(Connection conn) {

    }

    @Override
    public void refresh(Connection conn) {

    }

    /**
     * saves the Rating to db
     * @param conn
     */
    @Override
    public void save(Connection conn) {
        rateable.addRating(conn, this);
    }
}
