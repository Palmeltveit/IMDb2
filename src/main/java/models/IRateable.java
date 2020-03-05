package models;

import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;

public interface IRateable {

    void addRating(Connection conn, Rating rating);
    void addComment(Connection conn, Kommentar comment);

}
