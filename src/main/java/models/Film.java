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
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + " (Film, Person) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setLong(1, this.getID());
            statement.setLong(2, member.getPerson().getID());

            DBHelper.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("db error during save of Film CrewMember= " + e);
        }
    }

    public void addActor(Connection conn, Skuespiller skuespiller) {
        String tableName = "FilmSkuespiller";
        if(this.getID() == -1){
            this.save(conn);
        }
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + " (Film, Person, Rolle) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setLong(1, this.getID());
            statement.setLong(2, skuespiller.getPerson().getID());
            statement.setString(2, skuespiller.getRolle());

            DBHelper.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("db error during save of Film Skuespiller= " + e);
        }
    }

    public void initializeCrewMember(Connection conn, CrewMember member) {

    }

    public void initializeActor(Connection conn, Skuespiller actor) {

    }


}
