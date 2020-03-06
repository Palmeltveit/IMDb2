package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.*;
import java.util.List;

public class Film extends BaseFilm implements IFilm, IRateable {

    private List<Person> skuespillere;

    public Film(int id) {
        super(id);
    }

    public Film(Produksjonsselskap produksjonsselskap, String tittel, int lengde, int utgivelsesar,
                Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        super(produksjonsselskap, tittel, lengde, utgivelsesar, langeringsDato, beskrivelse, opprinneligLagetFor);
    }

    //TODO
    public void addRating(Connection conn, Rating rating) {
        DBHelper.addRating(conn, "FilmRating", "FilmID", this.getID(), rating);
    }

    public void addComment(Connection conn, Kommentar comment) {
        DBHelper.addComment(conn, "FilmKommentar", "FilmID", this.getID(), comment);
    }

    public void addCrewMember(Connection conn, CrewMember member) {
        String tableName = "Film" + member.getCrewTablePostfix();

        if(this.getID() == -1){
            this.save(conn);
        }
        DBHelper.addCrewMember(conn, tableName, "Film", this.getID(), member);
    }

    public void addActor(Connection conn, Skuespiller skuespiller) {
        String tableName = "FilmSkuespiller";
        if(this.getID() == -1){
            this.save(conn);
        }
        DBHelper.addActor(conn, tableName, "Film", this.getID(), skuespiller);
    }

}
