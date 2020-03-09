package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Film extends BaseFilm implements IFilm, IRateable {
    private List<Kategori> kategorier; //TODO: (Jøgga) "En film kan ha flere kategorier"

    public Film(long id) {
        super(id);
    }

    public List<Kategori> getKategorier() {
        return kategorier;
    }

    @Override
    public void initialize(Connection conn) {
        super.initialize(conn);

        kategorier = new ArrayList<>();

        // Eeeh, ikke helt fungerende kanskje?
        try (
            PreparedStatement stmt = conn.prepareStatement(
                   "SELECT Kategori.ID, Kategori.Navn, Kategori.Beskrivelse FROM `Film` " +
                           "INNER JOIN `FilmKategori` ON `Film`.ID = `FilmKategori`.Film " +
                           "INNER JOIN `Kategori` ON `FilmKategori`.Kategori = `Kategori`.ID " +
                           "WHERE `Film`.ID = ?"
            );
        ) {
            stmt.setLong(1, this.getID());
            try ( ResultSet rs = stmt.executeQuery(); ) {
                while (rs.next()) {
                    Kategori k = new Kategori(rs.getInt("ID"),
                            rs.getString("Navn"), rs.getString("Beskrivelse"));
                    // System.out.println("Got kategori: " + k.getID() + ", " + k.getNavn() + ", " + k.getBeskrivelse());
                    kategorier.add(k);
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of film kategori = "+e);
        }
    }

    @Override
    public void refresh(Connection conn) { initialize(conn); }

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

    public void addCategory(Connection conn, Kategori kategori) {
        if(this.getID() == -1){
            this.save(conn);
        }

        try (
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO FilmKategori (Film, Kategori) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ) {

            statement.setLong(1, getID());
            statement.setLong(2, kategori.getID());

            DBHelper.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("Error on adding film category");
            e.printStackTrace();
        }
    }

    public String toString() {
        return this.getTittel();
    }

}
