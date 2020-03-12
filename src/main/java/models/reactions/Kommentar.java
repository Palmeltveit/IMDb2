package models.reactions;

import DB.ActiveDomainObject;
import models.Bruker;
import models.IFilm;
import models.IRateable;

import java.sql.Connection;

/**
 * Class Kommentar corresponds to a comment on a rateable class (movie, series, episode)
 */
public class Kommentar implements ActiveDomainObject {

    private long ID = -1;
    private Bruker bruker;
    private IRateable rateable;

    private String tittel, innhold;

    /**
     *
     * @param ID - id of comment in db
     */
    public Kommentar(long ID) {
        this.ID = ID;
    }

    /**
     *
     * @param bruker - user commenting
     * @param film - movie being commented on
     * @param tittel - title of comment
     * @param innhold - body of comment
     */
    public Kommentar(Bruker bruker, IRateable film, String tittel, String innhold) {
        this.bruker = bruker;
        this.rateable = film;
        this.tittel = tittel;
        this.innhold = innhold;
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

    public IRateable getFilm() {
        return rateable;
    }

    public String getTittel() {
        return tittel;
    }

    public String getInnhold() {
        return innhold;
    }

    @Override
    public void initialize(Connection conn) {

    }

    @Override
    public void refresh(Connection conn) {

    }

    @Override
    public void save(Connection conn) {
        rateable.addComment(conn, this);
    }
}
