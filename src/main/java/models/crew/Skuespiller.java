package models.crew;

import DB.ActiveDomainObject;
import models.IFilm;
import models.Person;

import java.sql.Connection;

public class Skuespiller implements ActiveDomainObject {

    private IFilm film;
    private Person person;
    private String rolle;

    public void initialize(Connection conn) {
        film.initializeActor(conn, this);
    }

    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    public void save(Connection conn) {
        film.addActor(conn, this);
    }

    public Person getPerson() {
        return person;
    }

    public String getRolle() {
        return rolle;
    }
}
