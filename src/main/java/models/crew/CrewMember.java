package models.crew;

import DB.ActiveDomainObject;
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

    public void initialize(Connection conn) {
        film.initializeCrewMember(conn, this);
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
