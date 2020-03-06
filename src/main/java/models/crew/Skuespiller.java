package models.crew;

import DB.ActiveDomainObject;
import models.IFilm;
import models.Person;

import java.sql.Connection;

public class Skuespiller implements ActiveDomainObject {

    private IFilm film;
    private Person person;
    private String rolle;

    public Skuespiller(Person person, IFilm film, String rolle) {
        this.film = film;
        this.person = person;
        this.rolle = rolle;
    }

    public void initialize(Connection conn) {
        System.out.println("unødvendig å hente ut en spesifikk skuespiller i en spesifikk film -> ikke implementert");
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
