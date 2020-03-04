package models;

import DB.ActiveDomainObject;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;
import java.sql.Date;

public class Serie extends AbstraktFilm implements IRateable, ActiveDomainObject {

    public Serie(int id) {
        super(id);
    }

    public Serie(Produksjonsselskap produksjonsselskap, String tittel, int lengde, int utgivelsesar,
                 Date langeringsDato, String beskrivelse, int opprinneligLagetFor) {
        super(produksjonsselskap, tittel, lengde, utgivelsesar, langeringsDato, beskrivelse, opprinneligLagetFor);
    }

    //TODO
    public void addRating(Rating rating) {

    }

    public void addComment(Kommentar comment) {

    }

    public void initialize(Connection conn) {

    }

    public void refresh(Connection conn) {

    }

    public void save(Connection conn) {

    }
}
