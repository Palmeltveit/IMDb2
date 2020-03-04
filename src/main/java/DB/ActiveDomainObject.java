package DB;

import java.sql.*;

public interface ActiveDomainObject {
    void initialize (Connection conn);
    void refresh (Connection conn);
    void save (Connection conn);
}