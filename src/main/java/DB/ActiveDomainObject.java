package DB;

import java.sql.*;

/**
 * Interface for classes related to database-tables containing necessary DB-functionality.
 */
public interface ActiveDomainObject {
    void initialize (Connection conn);

    /**
     * Actually really unnecessary the way used in this project, but it was used in examples and thus kept by us.
     * @param conn
     */
    void refresh (Connection conn);
    void save (Connection conn);
}