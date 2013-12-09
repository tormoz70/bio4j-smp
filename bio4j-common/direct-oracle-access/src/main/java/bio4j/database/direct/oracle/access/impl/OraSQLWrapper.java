package bio4j.database.direct.oracle.access.impl;

import java.sql.SQLException;

/**
 * Базовый враппер для SQL
 */
public class OraSQLWrapper {
    private OraCommandImpl owner;
    public OraSQLWrapper(OraCommandImpl owner) {
        this.owner = owner;
    }

    public String prepare(String sql) throws SQLException {
        return sql;
    }
}
