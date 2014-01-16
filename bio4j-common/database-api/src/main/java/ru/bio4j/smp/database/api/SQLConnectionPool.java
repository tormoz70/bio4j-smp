package ru.bio4j.smp.database.api;

import java.sql.*;

public interface SQLConnectionPool {
    Connection getConnection() throws SQLException;
    Connection getConnection(String userName, String password) throws SQLException;
    SQLConnectionPoolStat getStat();
    void addAfterEvent(SQLConnectionAfterEvent e);
    void clearAfterEvents();
}
