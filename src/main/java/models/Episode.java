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

public class Episode implements ActiveDomainObject, IFilm, IRateable {

    private long ID = -1;
    private Serie serie;
    private int sesongNr, episodeNr;

    public Episode(long ID) {
        this.ID = ID;
    }

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


    public void addRating(Connection conn, Rating rating) {
        DBHelper.addRating(conn, "EpisodeRating", "EpisodeID", this.ID, rating);
    }

    public void addComment(Connection conn, Kommentar comment) {
        DBHelper.addComment(conn, "EpisodeKommentar", "EpisodeID", this.ID, comment);
    }

    public void addCrewMember(Connection conn, CrewMember member) {
        String tableName = "Episode" + member.getCrewTablePostfix();

        if(this.ID == -1){
            this.save(conn);
        }

        DBHelper.addCrewMember(conn, tableName, "Episode", this.ID, member);
    }

    public void addActor(Connection conn, Skuespiller skuespiller) {
        String tableName = "EpisodeSkuespiller";

        if(this.ID == -1){
            this.save(conn);
        }

        DBHelper.addActor(conn, tableName, "Episode", this.ID, skuespiller);
    }

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
