package models.crew;

import DB.ActiveDomainObject;
import jdk.jshell.spi.ExecutionControl;
import models.IFilm;
import models.Person;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static models.crew.CrewTypes.*;

public class CrewMember implements ActiveDomainObject {

    private IFilm film;
    private CrewTypes crewType;
    private Person person;

    private static Map<CrewTypes, String> TABLE_POSTFIXES = new HashMap<CrewTypes, String>(){{
        put(REGISSOR, "Regissør");
        put(MANUSFORFATTER, "Manusforfatter");
        put(KOMPONIST, "Komponist");
        put(MUSIKER, "Musiker");
    }};

    public CrewMember(Person person, IFilm film, CrewTypes crewType) {
        this.film = film;
        this.crewType = crewType;
        this.person = person;
    }

    public void initialize(Connection conn) {
        //unødvendig å bruke exceptions her for vår del
        System.out.println("unødvendig å hente ut et spesifikk crewmember i en spesifikk film -> ikke implementert");
    }

    public void refresh(Connection conn) {
        initialize(conn);
    }

    public void save(Connection conn) {
        film.addCrewMember(conn, this);
    }

    public Person getPerson() {
        return person;
    }

    public String getCrewTablePostfix(){
        return TABLE_POSTFIXES.get(this.crewType);
    }
}
