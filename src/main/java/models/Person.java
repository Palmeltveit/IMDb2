package models;

import DB.ActiveDomainObject;
import DB.DBConnection;
import DB.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Person implements ActiveDomainObject {

    private long ID;
    private String navn, fodselsland;
    private int fodselsar;

    public Person(long ID) {
        this.ID = ID;
    }

    public Person(String navn, String fodselsland, int fodselsar) {
        this.navn = navn;
        this.fodselsland = fodselsland;
        this.fodselsar = fodselsar;
    }

    public long getID() {
        return ID;
    }

    public String getNavn() {
        return navn;
    }

    public String getFodselsland() {
        return fodselsland;
    }

    public int getFodselsar() {
        return fodselsar;
    }

    @Override
    public void initialize(Connection conn) {
        try {
            PreparedStatement statement = conn.prepareStatement("Navn, Fødselsland, Fødselsår from Person where ID=?");
            statement.setLong(1, this.ID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.navn = rs.getString("Navn");
                this.fodselsland = rs.getString("Fødselsland");
                this.fodselsar = rs.getInt("Fødselsår");
            }
        } catch (Exception e) {
            System.out.println("db error during select of Person= " + e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    @Override
    public void save(Connection conn) {

        try {
            //assuming not in table -> inserting
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Person(Navn, Fødselsland, Fødselsår) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, navn);
            statement.setString(2, fodselsland);
            statement.setInt(3, fodselsar);

            this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);

        } catch (Exception e){
            System.out.println("db error during save of Person= " + e);
        }
    }
}
