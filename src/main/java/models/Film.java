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
    private Kategori kategori;

    public Film(long id) {
        super(id);
    }

    public Kategori getKategori() {
        return kategori;
    }

    @Override
    public void initialize(Connection conn) {
        super.initialize(conn);

        // Eeeh, ikke helt fungerende kanskje?
        try (
            PreparedStatement stmt = conn.prepareStatement(
                   "SELECT Kategori.ID, Kategori.Navn, Kategori.Beskrivelse FROM `Film` " +
                           "INNER JOIN `FilmKategori` ON `Film`.ID = ? " +
                           "INNER JOIN `Kategori` ON `FilmKategori`.Kategori = `Kategori`.ID"
            );
        ) {
            stmt.setLong(1, this.getID());
            try ( ResultSet rs = stmt.executeQuery(); ) {
                while (rs.next()) {
                    //getting production company
                    kategori = new Kategori(rs.getInt("ID"),
                            rs.getString("Navn"), rs.getString("Beskrivelse"));
                    break; // assume only 1
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of film kategori = "+e);
        }

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

}
