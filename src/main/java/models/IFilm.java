package models;

import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;

public interface IFilm {

    void addCrewMember(Connection conn, CrewMember member);
    void initializeCrewMember(Connection conn, CrewMember member);

    void addActor(Connection conn, Skuespiller actor);
    void initializeActor(Connection conn, Skuespiller actor);
}
