package models;

import DB.ActiveDomainObject;

import java.sql.Connection;

public class Bruker implements ActiveDomainObject {

    private int ID;
    private String brukernavn, passwordHash;

    @Override
    public void initialize(Connection conn) {

    }

    @Override
    public void refresh(Connection conn) {

    }

    @Override
    public void save(Connection conn) {

    }
}
