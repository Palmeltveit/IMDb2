package models.reactions;

import DB.ActiveDomainObject;
import models.Bruker;
import models.IFilm;
import models.IRateable;

import java.sql.Connection;

public class Rating implements ActiveDomainObject {

    private long ID;
    private Bruker bruker;
    private IRateable rateable;

    private String tittel, innhold;
    private int rating;

    public Rating(int ID) {
        this.ID = ID;
    }

    public Rating(Bruker bruker, IRateable rateable, String tittel, String innhold, int rating) {
        this.bruker = bruker;
        this.rateable = rateable;
        this.tittel = tittel;
        this.innhold = innhold;
        this.rating = rating;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public Bruker getBruker() {
        return bruker;
    }

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

    @Override
    public void save(Connection conn) {
        rateable.addRating(conn, this);
    }
}
