package models;

import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;

/**
 * An interface to simplify adding CrewMembers and actors to the various types of Film (Film, series, episode)
 */
public interface IFilm {

    void addCrewMember(Connection conn, CrewMember member);
    void addActor(Connection conn, Skuespiller actor);
}
