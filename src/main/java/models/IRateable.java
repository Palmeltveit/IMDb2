package models;

import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;

/**
 * An Interface to simplify the rating and commenting on rateable stuff in the db (series, episode, film)
 */
public interface IRateable {

    void addRating(Connection conn, Rating rating);
    void addComment(Connection conn, Kommentar comment);

}
