package models;

import models.reactions.Kommentar;
import models.reactions.Rating;

public interface IRateable {

    void addRating(Rating rating);
    void addComment(Kommentar comment);

}
