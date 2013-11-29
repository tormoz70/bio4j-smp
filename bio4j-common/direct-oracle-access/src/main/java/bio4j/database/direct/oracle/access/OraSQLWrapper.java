package bio4j.database.direct.oracle.access;

import java.sql.SQLException;

/**
 * Базовый враппер для SQL
 */
public class OraSQLWrapper {

    public String prepare(String sql) throws SQLException {
        return sql;
    }
}
