package models;

import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;

public class Episode implements IFilm {

    private int ID;
    private Serie serie;
    private int sesongNr, episodeNr;

    //TODO
    public void addRating(Rating rating) {

    }

    public void addComment(Kommentar comment) {

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
}
