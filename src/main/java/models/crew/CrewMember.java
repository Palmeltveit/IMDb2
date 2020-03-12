package models.crew;

import DB.ActiveDomainObject;
import jdk.jshell.spi.ExecutionControl;
import models.IFilm;
import models.Person;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static models.crew.CrewTypes.*;

/**
 * Class CrewMember corresponds to all types of crewmembers either in movies or eposodes
 */
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

    /**
     *
     * @param person - the person corresponding to the role as crewmember
     * @param film - the film it is a crew member of
     * @param crewType - type of crew-member
     */
    public CrewMember(Person person, IFilm film, CrewTypes crewType) {
        this.film = film;
        this.crewType = crewType;
        this.person = person;
    }

    /**
     * does nothing - never need to retrieve a single CrewMember
     * @param conn - connection to database
     */
    public void initialize(Connection conn) {
        //unødvendig å bruke exceptions her for vår del
        System.out.println("unødvendig å hente ut et spesifikk crewmember i en spesifikk film -> ikke implementert");
    }

    /**
     * Really does nothing else than call initialize again, not really necessary
     * @param conn
     */
    public void refresh(Connection conn) {
        initialize(conn);
    }

    /**
     * Saves the crewMember to database, if not already saved.
     * @param conn - connection to database
     */
    public void save(Connection conn) {
        film.addCrewMember(conn, this);
    }

    /**
     *
     * @return Person person - person in this crewmember role
     */
    public Person getPerson() {
        return person;
    }

    /**
     *
     * @return String postfix of table name corresponding to the crewmember type (specific for our database design)
     */
    public String getCrewTablePostfix(){
        return TABLE_POSTFIXES.get(this.crewType);
    }
}
