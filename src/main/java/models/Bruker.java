package models;

import DB.ActiveDomainObject;
import DB.DBHelper;
import algorithms.ProfessionalHashingAlgorithm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Bruker implements ActiveDomainObject {

    private long ID = -1;
    private String brukernavn, passwordHash;

    /**
     * initialized user, hashes password professionally and saves it in variable passwordHash
     * @param brukernavn - username
     * @param password - password in plain text
     */
    public Bruker(String brukernavn, String password) {
        this.brukernavn = brukernavn;
        this.passwordHash = ProfessionalHashingAlgorithm.encrypt(password, 4);
    }

    public boolean isLoggedIn(){
        return this.ID != -1;
    }

    public long getID() {
        return ID;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     *
     * @param conn - db connection
     * @return True if username is unique - not in db
     */
    private boolean checkIfUsernameUnique(Connection conn){
        try (
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Bruker WHERE Brukernavn=?");
        ){
            statement.setString(1, this.brukernavn);
            ResultSet rs = statement.executeQuery();

            return !rs.next();
        } catch (Exception e) {
            System.out.println("db error during select or save of Bruker= "+e);
        }
        return false;
    }

    /**
     * retrieves user from db based on username and passwordHash
     * @param conn - db connection
     */
    @Override
    public void initialize(Connection conn) {
        try (
            PreparedStatement statement = conn.prepareStatement("SELECT ID from Bruker where Brukernavn=? AND PassordHash=?");
        ){
            statement.setString(1, this.brukernavn);
            statement.setString(2, this.passwordHash);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.ID = rs.getLong("ID");
            }
        } catch (Exception e) {
            System.out.println("db error during select of production company= " + e);
        }
    }

    @Override
    public void refresh(Connection conn) {
        this.initialize(conn);
    }

    /**
     * Saves user based on given username and hashed password.
     * @param conn - db-connection
     * @throws RuntimeException if user with given username already exists
     */
    @Override
    public void save(Connection conn) throws RuntimeException {
        if(this.ID == -1){ // id == -1 <=> should be saved
            if(checkIfUsernameUnique(conn)) {
                try (
                    PreparedStatement statement = conn.prepareStatement(
                            "INSERT INTO Bruker(Brukernavn, PassordHash) VALUES (?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS);
                ){
                    statement.setString(1, this.brukernavn);
                    statement.setString(2, this.passwordHash);

                    this.ID = DBHelper.executeAndCheckInsertWithReturnId(statement);

                } catch (Exception e) {
                    System.out.println("db error during select or save of Bruker= " + e);
                    return;
                }
            } else {
                throw new RuntimeException("User with specified username already exists - you must choose another");
            }
        }
    }
}
