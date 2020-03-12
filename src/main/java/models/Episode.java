package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * An episode of a Series
 */
public class Episode implements ActiveDomainObject, IFilm, IRateable {

    private long ID = -1;
    private Serie serie;
    private int sesongNr, episodeNr;

    /**
     * Initializes episode based on id in db
     * @param ID - id in db
     */
    public Episode(long ID) {
        this.ID = ID;
    }

    /**
     *
     * @param serie Serie the episode is part of
     * @param sesongNr Season number
     * @param episodeNr Episode number in season
     */
    public Episode(Serie serie, int sesongNr, int episodeNr) {
        this.serie = serie;
        this.sesongNr = sesongNr;
        this.episodeNr = episodeNr;
    }

    public long getID() {
        return ID;
    }

    public Serie getSerie() {
        return serie;
    }

    public int getSesongNr() {
        return sesongNr;
    }

    public int getEpisodeNr() {
        return episodeNr;
    }


    /**
     * Adds a user rating to the episode
     * @param conn - db connection
     * @param rating - Rating to be added to episode
     */
    public void addRating(Connection conn, Rating rating) {
        DBHelper.addRating(conn, "EpisodeRating", "EpisodeID", this.ID, rating);
    }

    /**
     * Adds a user comment to the episode
     * @param conn
     * @param comment
     */
    public void addComment(Connection conn, Kommentar comment) {
        DBHelper.addComment(conn, "EpisodeKommentar", "EpisodeID", this.ID, comment);
    }


    /**
     * Adds a crewmember to the episode
     * @param conn
     * @param member
     */
    public void addCrewMember(Connection conn, CrewMember member) {
        String tableName = "Episode" + member.getCrewTablePostfix();

        if(this.ID == -1){
            this.save(conn);
        }

        DBHelper.addCrewMember(conn, tableName, "Episode", this.ID, member);
    }

    /**
     * Adds an actor to the episode
     * @param conn
     * @param skuespiller
     */
    public void addActor(Connection conn, Skuespiller skuespiller) {
        String tableName = "EpisodeSkuespiller";

        if(this.ID == -1){
            this.save(conn);
        }

        DBHelper.addActor(conn, tableName, "Episode", this.ID, skuespiller);
    }

    /**
     * initialized from db based on id
     * @param conn
     */
    @Override
    public void initialize(Connection conn) {
        try (
            PreparedStatement statement = conn.prepareStatement("select * from Episode where ID=?");
        ){
            statement.setLong(1, this.ID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    //getting production company
                    Serie serie = new Serie(rs.getLong("SerieID"));
                    serie.initialize(conn);

                    this.serie = serie;
                    this.sesongNr = rs.getInt("SesongNr");
                    this.episodeNr = rs.getInt("EpisodeNr");
                }
            }
        } catch (Exception e) {
            System.out.println("db error during select of episode= "+e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    /**
     * Saves to db if not already saved (if id is not yet set)
     * @param conn
     */
    @Override
    public void save(Connection conn) {

        if (this.ID == -1 && this.serie != null) { //assuming ID == -1 <=> should be saved

            this.serie.save(conn);
            if (this.serie.getSerieID() != -1) {
                try (
                    PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO Episode(SerieID, SesongNr, EpisodeNr) VALUES (?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ){
                    statement.setLong(1, this.serie.getSerieID());
                    statement.setInt(2, this.sesongNr);
                    statement.setInt(3, this.episodeNr);

                    this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);

                } catch (Exception e) {
                    System.out.println("db error during save of Episode= " + e);
                }
            }
        }
    }
}
