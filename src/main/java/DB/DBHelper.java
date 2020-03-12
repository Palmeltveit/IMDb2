package DB;

import models.Film;
import models.Produksjonsselskap;
import models.crew.CrewMember;
import models.crew.Skuespiller;
import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A helper class containing static methods to avoid repeating similar code
 */
public class DBHelper {

    /**
     * Executes an already prepared PreparedStatement and returns the id
     * @param statement The pre-prepared preparedStatement.
     * @return Long id, id of row inserted
     * @throws Exception if execution failed to return an id.
     */
    public static long executeAndCheckInsertWithReturnId(PreparedStatement statement) throws Exception {
        executeAndCheckInsert(statement);
        ResultSet generatedKeysSet = statement.getGeneratedKeys();
        if(generatedKeysSet.next()){
            long id = generatedKeysSet.getLong(1);
            generatedKeysSet.close();

            return id;
        } else {
            generatedKeysSet.close();
            throw new RuntimeException("failed to retrieve id, RETURN_GENERATED_KEYS might not be set.");
        }
    }

    /**
     * Executes a pre-prepared PreparedStatement and checks that something was really inserted
     * @param statement a pre-prepared preparedStatement.
     * @throws SQLException if no new rows were inserted through the execution of the statement.
     */
    public static void executeAndCheckInsert(PreparedStatement statement) throws SQLException {
        int insertedRows = statement.executeUpdate();
        if(insertedRows == 0){
            throw new SQLException("failed to insert new Film row");
        }
    }

    /**
     * Adds a rating to one of the several rating-tables in our database
     * @param conn DB-connection
     * @param tableName Name of the rating-table to insert into
     * @param FKName Name of the foreign-key column in the specified rating-table
     * @param id ID to put into the foreign-key column
     * @param rating the Rating to insert.
     */
    public static void addRating(Connection conn, String tableName, String FKName, long id, Rating rating){
        try (
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + "(BrukerID, " + FKName + ", Tittel, Innhold, Rating) values (?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
        ){
            statement.setLong(1, rating.getBruker().getID());
            statement.setLong(2, id);
            statement.setString(3, rating.getTittel());
            statement.setString(4, rating.getInnhold());
            statement.setInt(5, rating.getRating());

            rating.setID(executeAndCheckInsertWithReturnId(statement));

        } catch (Exception e) {
            System.out.println("db error during select of Serie Rating= "+e);
            return;
        }
    }

    /**
     * Adds a comment to one of the several comment-tables in our database
     * @param conn DB-connection
     * @param tableName Name of the comment-table to insert into
     * @param FKName Name of the foreign-key column in the specified comment-table
     * @param id ID to put into the foreign-key column
     * @param comment the comment to insert
     */
    public static void addComment(Connection conn, String tableName, String FKName, long id, Kommentar comment){
        try (
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + "(BrukerID, " + FKName + ", Tittel, Innhold) values (?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
        ){
            statement.setLong(1, comment.getBruker().getID());
            statement.setLong(2, id);
            statement.setString(3, comment.getTittel());
            statement.setString(4, comment.getInnhold());

            comment.setID(executeAndCheckInsertWithReturnId(statement));

        } catch (Exception e) {
            System.out.println("db error during select of Serie Rating= "+e);
            return;
        }
    }

    /**
     * adds a CrewMember to one of the (extremely) many crewmember-tables in our database
     * @param conn DB-connection
     * @param tableName Name of the crewMember-table to insert into (based on filmType and crewType)
     * @param FKName Name of the foreign-key column in the specified crewMember-table
     * @param id ID to put into the foreign-key column
     * @param member the member
     */
    public static void addCrewMember(Connection conn, String tableName, String FKName, long id, CrewMember member){
        try (
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + " (" + FKName + ", Person) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
        ){
            statement.setLong(1, id);
            statement.setLong(2, member.getPerson().getID());

            System.out.println("addCrewMember SQL: " + statement.toString());

            DBHelper.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("db error during save of CrewMember= " + e);
        }
    }

    /**
     * adds a CrewMember to one of the many actor-tables in our database
     * @param conn DB-connection
     * @param tableName Name of the actor-table to insert into (based on filmType)
     * @param FKName Name of the foreign-key column in the specified actor-table
     * @param id ID to put into the foreign-key column
     * @param skuespiller the actor to insert
     */
    public static void addActor(Connection conn, String tableName, String FKName, long id, Skuespiller skuespiller){
        try (
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + " (" + FKName + ", Person, Rolle) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
        ){
            statement.setLong(1, id);
            statement.setLong(2, skuespiller.getPerson().getID());
            statement.setString(3, skuespiller.getRolle());

            DBHelper.executeAndCheckInsert(statement);

        } catch (Exception e){
            System.out.println("db error during save of Skuespiller= " + e);
        }
    }
}
