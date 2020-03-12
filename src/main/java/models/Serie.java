package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A class corresponding to a Serie in the database,
 * contains all methods special to Series, not shared with Film through BaseFilm.
 */
public class Serie extends BaseFilm implements IRateable, ActiveDomainObject {

    private long serieID = -1;

    /**
     * Initializes series based on id of instance already in the database
     * @param serieId Id of Serie in database (not the Film ID)
     */
    public Serie(long serieId) {
        super(-1);
        this.serieID = serieId;
    }

    /**
     * Initializes series based on input in the app, assuming not already existing in the db.
     * Only forwards values to super (BaseFilm) for normal initialization.
     * @param produksjonsselskap
     * @param tittel
     * @param lengde
     * @param utgivelsesar
     * @param langeringsDato
     * @param beskrivelse
     * @param opprinneligLagetFor
     */
    public Serie(Produksjonsselskap produksjonsselskap, String tittel, int lengde, int utgivelsesar,
                 Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        super(produksjonsselskap, tittel, lengde, utgivelsesar, langeringsDato, beskrivelse, opprinneligLagetFor);
    }

    /**
     * Adds Series-specific rating
     * @param conn
     * @param rating
     */
    public void addRating(Connection conn, Rating rating) {
        DBHelper.addRating(conn, "SerieRating", "SerieID", this.serieID, rating);
    }

    /**
     * Adds Series-specific comment
     * @param conn
     * @param comment
     */
    public void addComment(Connection conn, Kommentar comment) {
        DBHelper.addComment(conn, "SerieKommentar", "SerieID", this.serieID, comment);
    }

    public long getSerieID() {
        return serieID;
    }

    public void initialize(Connection conn) {
        try (
            PreparedStatement stmt = conn.prepareStatement("select * from Serie where ID=?");
        ){
            stmt.setLong(1, this.serieID);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    this.setID(rs.getLong("FilmID"));
                    super.initialize(conn);
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of Serie= "+e);
            return;
        }
    }

    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    public void save(Connection conn) {

        try {
            //inserting baseFilm row first, then getting the id
            super.save(conn);
            if(super.getID() != -1) { //save successful
                //assuming not in table -> inserting
                try(
                    PreparedStatement statement = conn.prepareStatement(
                            "INSERT INTO Serie(FilmID) VALUES (?)",
                            PreparedStatement.RETURN_GENERATED_KEYS);
                ) {
                    statement.setLong(1, super.getID());

                    this.serieID = DBHelper.executeAndCheckInsertWithReturnId(statement);
                }
            }
        } catch (Exception e){
            System.out.println("db error during save of Film= " + e);
        }
    }
}
