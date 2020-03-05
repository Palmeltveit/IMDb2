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

public class Episode implements ActiveDomainObject, IFilm, IRateable {

    private int ID;
    private Serie serie;
    private int sesongNr, episodeNr;

    //TODO
    public void addRating(Connection conn, Rating rating) {
        DBHelper.addRating(conn, "EpisodeRating", "EpisodeID", this.ID, rating);
    }

    public void addComment(Connection conn, Kommentar comment) {
        DBHelper.addComment(conn, "EpisodeKommentar", "EpisodeID", this.ID, comment);
    }

    public void addCrewMember(Connection conn, CrewMember member) {
        String tableName = "Episode" + member.getCrewTablePostfix();
    }

    public void initializeCrewMember(Connection conn, CrewMember member) {

    }

    public void addActor(Connection conn, Skuespiller actor) {
        String tableName = "EpisodeSkuespiller";
    }

    public void initializeActor(Connection conn, Skuespiller actor) {

    }

    @Override
    public void initialize(Connection conn) {

    }

    @Override
    public void refresh(Connection conn) {

    }

    @Override
    public void save(Connection conn) {

    }
}
