package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.crew.Skuespiller;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Produksjonsselskap implements ActiveDomainObject {

    private long ID = -1;
    private String navn;
    private Date opprettet;

    public Produksjonsselskap(int id){
        this.ID = id;
    }

    public Produksjonsselskap(String navn, Date opprettet){
        this.navn = navn;
        this.opprettet = opprettet;
    }

    public Produksjonsselskap(long id, String navn, Date opprettet){
        this.ID = id;
        this.navn = navn;
        this.opprettet = opprettet;
    }

    public long getID() {
        return ID;
    }

    public String getNavn() {
        return navn;
    }

    public Date getOpprettet() {
        return opprettet;
    }

    public void initialize(Connection conn) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT Navn, Opprettet from Produksjonsselskap where ID=?");
            statement.setLong(1, this.ID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.navn = rs.getString("Navn");
                this.opprettet = rs.getDate("Opprettet");
            }
        } catch (Exception e) {
            System.out.println("db error during select of production company= " + e);
        }
    }

    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    public void save(Connection conn) {

        try {
            if(this.ID == -1) {
                //assuming not in table -> inserting
                PreparedStatement statement = conn.prepareStatement("INSERT INTO Produksjonsselskap(Navn, Opprettet) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.closeOnCompletion();
                statement.setString(1, this.navn);
                statement.setDate(2, this.opprettet);
                this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);
            } else {
                //assuming already in db -> updating (probably unnecessary, dropping this in the future)
                PreparedStatement statement = conn.prepareStatement("UPDATE Produksjonsselskap SET Navn=?, Opprettet=? WHERE ID=?");
                statement.setString(1, this.navn);
                statement.setDate(2, this.opprettet);
                statement.setLong(3, this.ID);
                statement.executeUpdate();
            }
        } catch (Exception e){
            System.out.println("db error during save of production company= " + e);
        }
    }

    /*
    static public List<Produksjonsselskap> findLargestProductionCompanyForGenre(
            Connection conn, String genre) {
        ArrayList<Produksjonsselskap> list = new ArrayList<>();

        try (
                PreparedStatement stm = conn.prepareStatement(
                        "SELECT Film, Rolle FROM `FilmSkuespiller` WHERE `Person` = ?"
                );

        ) {
            stm.setLong(1, this.ID);
            try ( ResultSet filmRs = filmerStm.executeQuery(); ) {
                while (filmRs.next()) {
                    Film film = new Film(filmRs.getInt("Film"));
                    film.initialize(conn);
                    String rolle = filmRs.getString("Rolle");
                    list.add(new Skuespiller(this, film, rolle));
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of Person= " + e);
        }
        return list;
    }
    */
}
