package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.Inserteable;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.*;
import java.util.List;

public class Film extends AbstraktFilm implements IFilm, IRateable, ActiveDomainObject {

    private List<Person> skuespillere;

    public Film(int id) {
        super(id);
    }

    public Film(Produksjonsselskap produksjonsselskap, String tittel, int lengde, int utgivelsesar,
                Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        super(produksjonsselskap, tittel, lengde, utgivelsesar, langeringsDato, beskrivelse, opprinneligLagetFor);
    }

    //TODO
    public void addRating(Rating rating) {

    }

    public void addComment(Kommentar comment) {

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

            DBConnection.executeAndCheckInsert(statement);

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

            DBConnection.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("db error during save of Film Skuespiller= " + e);
        }
    }

    public void initializeCrewMember(Connection conn, CrewMember member) {

    }

    public void initializeActor(Connection conn, Skuespiller actor) {

    }

    public void initialize(Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from Film where ID=?");
            stmt.setLong(1, this.getID());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //getting production company
                Produksjonsselskap produksjonsselskap = new Produksjonsselskap(rs.getInt("Produksjonsselskap"));
                produksjonsselskap.initialize(conn);

                this.initAbstraktFilmValues(produksjonsselskap,
                        rs.getString("Tittel"), rs.getInt("Lengde"),
                        rs.getInt("Utgivelsesår"), rs.getDate("LanseringsDato"),
                        rs.getString("Beskrivelse"), rs.getInt("OpprinneligLagetFor"));
            }

        } catch (Exception e) {
            System.out.println("db error during select of film= "+e);
            return;
        }
    }

    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    public void save(Connection conn) {

        try {
            //assuming not in table -> inserting
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Film(Produksjonsselskap, Tittel, Lengde, Utgivelsesår, " +
                            "LanseringsDato, Beskrivelse, OpprinneligLagetFor) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setLong(1, this.getProduksjonsselskap().getID());
            statement.setString(2, this.getTittel());
            statement.setInt(3, this.getLengde());
            statement.setInt(4, this.getUtgivelsesar());
            statement.setDate(5, this.getLangeringsDato());
            statement.setString(6, this.getBeskrivelse());
            statement.setInt(7, this.getOpprinneligLagetFor());

            this.setID(DBConnection.executeAndCheckInsertWithReturnId(statement));

        } catch (Exception e){
            System.out.println("db error during save of Film= " + e);
        }
    }
}
