package ru.bio4j.smp.database.api;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 */
public interface SQLConnectionAfterEvent {
    void handle(SQLConnectionPool sender, SQLConnectionAfterEventAttrs attrs) throws SQLException;
}
