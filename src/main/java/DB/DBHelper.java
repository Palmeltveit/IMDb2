package DB;

import models.reactions.Kommentar;
import models.reactions.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {

    public static long executeAndCheckInsertWithReturnId(PreparedStatement statement) throws Exception{
        executeAndCheckInsert(statement);
        ResultSet generatedKeysSet = statement.getGeneratedKeys();
        if(generatedKeysSet.next()){
            return generatedKeysSet.getLong(1);
        } else {
            throw new RuntimeException("failed to retrieve id, RETURN_GENERATED_KEYS might not be set.");
        }
    }

    public static void executeAndCheckInsert(PreparedStatement statement) throws SQLException {
        int insertedRows = statement.executeUpdate();
        if(insertedRows == 0){
            throw new SQLException("failed to insert new Film row");
        }
    }

    public static void addRating(Connection conn, String tableName, String FKName, long id, Rating rating){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + "(BrukerID, " + FKName + ", Tittel, Innhold, Rating) values (?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

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

    public static void addComment(Connection conn, String tableName, String FKName, long id, Kommentar comment){
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + tableName + "(BrukerID, " + FKName + ", Tittel, Innhold) values (?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

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
}
