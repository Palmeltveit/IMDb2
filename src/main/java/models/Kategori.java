package models;

import DB.ActiveDomainObject;
import DB.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Kategori implements ActiveDomainObject {

    private long ID = -1;

    private String navn, beskrivelse;

    public Kategori(long id) { this.ID = id; }

    public Kategori(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public Kategori(long id, String navn, String beskrivelse) {
        this.ID = id;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public long getID() {
        return ID;
    }
    public String getNavn() { return navn; }
    public String getBeskrivelse() { return beskrivelse; }


    @Override
    public void initialize(Connection conn) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT Navn, Beskrivelse from Kategori where ID=?");
            statement.setLong(1, this.ID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.navn = rs.getString("Navn");
                this.beskrivelse = rs.getString("Beskrivelse");
            }
        } catch (Exception e) {
            System.out.println("db error during select of Kategori= " + e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    @Override
    public void save(Connection conn) {
        try {
            if(this.ID == -1) {
                //assuming not in table -> inserting
                PreparedStatement statement = conn.prepareStatement("INSERT INTO Kategori (Navn, Beskrivelse) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, this.navn);
                statement.setString(2, this.beskrivelse);
                this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);
                statement.closeOnCompletion();
                System.out.println("Created new kategori: " + this.ID + " " + this.navn + " " + this.beskrivelse);
            }
        } catch (Exception e){
            System.out.println("db error during save of production company= " + e);
        }
    }

    public List<Film> findAllFilmsByCategory(Connection conn) {
        List<Film> list = new ArrayList<>();

        try (
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT Film.ID as ID FROM Film " +
                            "INNER JOIN FilmKategori ON Film.ID = FilmKategori.Film " +
                            "WHERE FilmKategori.Kategori = ?"
                );
        ) {
            stmt.setLong(1, this.getID());
            try ( ResultSet rs = stmt.executeQuery(); ) {
                while (rs.next()) {
                    Film f = new Film(rs.getInt("ID"));
                    f.initialize(conn); // not efficient but whatever
                    list.add(f);
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of film kategori = "+e);
        }
        return list;
    }

    static public List<Kategori> findAllCategories(Connection conn) {
        List<Kategori> list = new ArrayList<>();

        try (
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT ID, Navn, Beskrivelse from Kategori"
                );
        ) {
            try ( ResultSet rs = stmt.executeQuery(); ) {
                while (rs.next()) {
                    list.add(new Kategori(
                            rs.getInt("ID"),
                            rs.getString("Navn"),
                            rs.getString("Beskrivelse")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of film kategori = "+e);
        }
        return list;
    }
}
