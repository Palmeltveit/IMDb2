package models;

import DB.ActiveDomainObject;
import DB.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Kategori implements ActiveDomainObject {

    private long ID = -1;
    private String navn, beskrivelse;


    @Override
    public void initialize(Connection conn) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT Navn, Beskrivelse from Kategori where ID=?");
            statement.setLong(1, this.ID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.navn = rs.getString("Navn");
                this.beskrivelse = rs.getString("Beskrivelse");
            }
        } catch (Exception e) {
            System.out.println("db error during select of Kategori= " + e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    @Override
    public void save(Connection conn) {
        try {
            if(this.ID == -1) {
                //assuming not in table -> inserting
                PreparedStatement statement = conn.prepareStatement("INSERT INTO Kategori(Navn, Beskrivelse) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, this.navn);
                statement.setString(2, this.beskrivelse);
                this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);
            }
        } catch (Exception e){
            System.out.println("db error during save of production company= " + e);
        }
    }
}
