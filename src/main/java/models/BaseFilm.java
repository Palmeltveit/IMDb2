package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * BaseFilm contains the shared functionality between Film and Serie
 */
public class BaseFilm implements ActiveDomainObject {

    private long ID = -1; //-1 means no id yet => not saved in database

    private Produksjonsselskap produksjonsselskap;
    private String tittel;
    private int lengde;
    private int utgivelsesar;
    private Date langeringsDato;
    private String beskrivelse;
    private int opprinneligLagetFor;

    private List<Kategori> categoryList;

    public BaseFilm(long id){
        this.ID = id;
    }

    /**
     *
     * @param produksjonsselskap - Production company producing the movie/series
     * @param tittel - Title of movie
     * @param lengde - length
     * @param utgivelsesar - release year
     * @param langeringsDato - release date
     * @param beskrivelse - short description of movie
     * @param opprinneligLagetFor - medium it was originally created for (cinema/streamig/...)
     */
    public BaseFilm(Produksjonsselskap produksjonsselskap, String tittel, int lengde,
                    int utgivelsesar, Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        this.produksjonsselskap = produksjonsselskap;
        this.tittel = tittel;
        this.lengde = lengde;
        this.utgivelsesar = utgivelsesar;
        this.langeringsDato = langeringsDato;
        this.beskrivelse = beskrivelse;
        this.opprinneligLagetFor = opprinneligLagetFor;
    }

    /**
     * Like constructor initializes, only after getting values from db based on id.
     * @param produksjonsselskap
     * @param tittel
     * @param lengde
     * @param utgivelsesar
     * @param langeringsDato
     * @param beskrivelse
     * @param opprinneligLagetFor
     */
    protected void initAbstraktFilmValues(Produksjonsselskap produksjonsselskap, String tittel, int lengde,
                int utgivelsesar, Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        this.produksjonsselskap = produksjonsselskap;
        this.tittel = tittel;
        this.lengde = lengde;
        this.utgivelsesar = utgivelsesar;
        this.langeringsDato = langeringsDato;
        this.beskrivelse = beskrivelse;
        this.opprinneligLagetFor = opprinneligLagetFor;
    }

    /**
     *
     * @return Long id
     */
    public long getID() {
        return ID;
    }

    /**
     *
     * @return Produksjonsselskap Production company
     */
    public Produksjonsselskap getProduksjonsselskap() {
        return produksjonsselskap;
    }

    public String getTittel() {
        return tittel;
    }

    public int getLengde() {
        return lengde;
    }

    public int getUtgivelsesar() {
        return utgivelsesar;
    }

    public Date getLangeringsDato() {
        return langeringsDato;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public int getOpprinneligLagetFor() {
        return opprinneligLagetFor;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Initializes and retrieves from db based on id, creates and initialized instance of production company as well.
     * @param conn - db-connection
     */
    public void initialize(Connection conn) {
        try (
            PreparedStatement stmt = conn.prepareStatement("select * from Film where ID=?");
        ){
            stmt.setLong(1, this.getID());
            try (
                ResultSet rs = stmt.executeQuery();
            ) {
                while (rs.next()) {
                    //getting production company
                    Produksjonsselskap produksjonsselskap = new Produksjonsselskap(rs.getInt("Produksjonsselskap"));
                    produksjonsselskap.initialize(conn);

                    this.initAbstraktFilmValues(produksjonsselskap,
                            rs.getString("Tittel"), rs.getInt("Lengde"),
                            rs.getInt("Utgivelsesår"), rs.getDate("LanseringsDato"),
                            rs.getString("Beskrivelse"), rs.getInt("OpprinneligLagetFor"));
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of film= "+e);
        }
    }

    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    /**
     * Saves the film.
     * @param conn - db-connection
     */
    public void save(Connection conn) {
        if (this.ID == -1) { //assuming ID == -1 <=> should be saved
            try (PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO Film(Produksjonsselskap, Tittel, Lengde, Utgivelsesår, " +
                                "LanseringsDato, Beskrivelse, OpprinneligLagetFor) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
            ){
                statement.setLong(1, this.getProduksjonsselskap().getID());
                statement.setString(2, this.getTittel());
                statement.setInt(3, this.getLengde());
                statement.setInt(4, this.getUtgivelsesar());
                statement.setDate(5, this.getLangeringsDato());
                statement.setString(6, this.getBeskrivelse());
                statement.setInt(7, this.getOpprinneligLagetFor());

                this.setID(DBHelper.executeAndCheckInsertWithReturnId(statement));

            } catch (Exception e) {
                System.out.println("db error during save of Film= " + e);
            }
        }
    }
}
