package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.crew.Skuespiller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * A class corresponding to Person table in the database,
 * contains methods special to person.
 */
public class Person implements ActiveDomainObject {

    private long ID;
    private String navn, fodselsland;
    private int fodselsar;

    public Person(long ID) {
        this.ID = ID;
    }

    public Person(String navn, String fodselsland, int fodselsar) {
        this.navn = navn;
        this.fodselsland = fodselsland;
        this.fodselsar = fodselsar;
    }

    public long getID() {
        return ID;
    }

    public String getNavn() {
        return navn;
    }

    public String getFodselsland() {
        return fodselsland;
    }

    public int getFodselsar() {
        return fodselsar;
    }

    @Override
    public void initialize(Connection conn) {
        try(
            PreparedStatement statement = conn.prepareStatement("SELECT Navn, Fødselsland, Fødselsår from Person where ID=?");
        ) {
            statement.setLong(1, this.ID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.navn = rs.getString("Navn");
                this.fodselsland = rs.getString("Fødselsland");
                this.fodselsar = rs.getInt("Fødselsår");
            }
        } catch (Exception e) {
            System.out.println("db error during select of Person= " + e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    @Override
    public void save(Connection conn) {

        try (
            //assuming not in table -> inserting
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Person(Navn, Fødselsland, Fødselsår) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ) {

            statement.setString(1, navn);
            statement.setString(2, fodselsland);
            statement.setInt(3, fodselsar);

            this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);

        } catch (Exception e){
            System.out.println("db error during save of Person= " + e);
        }
    }

    public List<Skuespiller> findAllActorRoles(Connection conn) {
        ArrayList<Skuespiller> list = new ArrayList<>();

        try (
            PreparedStatement filmerStm = conn.prepareStatement(
                    "SELECT Film, Rolle FROM `FilmSkuespiller` WHERE `Person` = ?"
            );
            PreparedStatement episoderStm = conn.prepareStatement(
                    "SELECT Episode, Rolle FROM `EpisodeSkuespiller` WHERE `Person` = ?"
            );

        ) {
            filmerStm.setLong(1, this.ID);
            try ( ResultSet filmRs = filmerStm.executeQuery(); ) {
                while (filmRs.next()) {
                    Film film = new Film(filmRs.getInt("Film"));
                    film.initialize(conn);
                    String rolle = filmRs.getString("Rolle");
                    list.add(new Skuespiller(this, film, rolle));
                }
            }

            episoderStm.setLong(1, this.ID);
            try ( ResultSet episoderRs = episoderStm.executeQuery(); ) {
                while (episoderRs.next()) {
                    Episode episode = new Episode(episoderRs.getInt("Episode"));
                    episode.initialize(conn);
                    String rolle = episoderRs.getString("Rolle");
                    list.add(new Skuespiller(this, episode, rolle));
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of Person= " + e);
        }
        return list;
    }

    static public List<Person> findAllByName(Connection conn, String nameLike) {
        ArrayList<Person> list = new ArrayList<>();

        try (
            PreparedStatement personsStm = conn.prepareStatement(
                    "SELECT ID, Fødselsland, Fødselsår, Navn FROM `Person` WHERE `Navn` LIKE ?"
            );
        ) {
            personsStm.setString(1, nameLike);
            try ( ResultSet personsRs = personsStm.executeQuery(); ) {
                while (personsRs.next()) {
                    Person person = new Person(
                            personsRs.getString("Navn"),
                            personsRs.getString("Fødselsland"),
                            personsRs.getInt("Fødselsår"));
                    person.ID = personsRs.getInt("ID");
                    list.add(person);
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of Person= " + e);
        }
        return list;
    }

    static public Optional<Person> findByName(Connection conn, String nameLike) {
        return findAllByName(conn, nameLike).stream().findFirst();
    }
}
