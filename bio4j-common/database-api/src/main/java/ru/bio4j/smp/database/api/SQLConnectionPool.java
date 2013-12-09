package ru.bio4j.smp.database.api;

import java.sql.*;

public interface SQLConnectionPool {
    Connection getConnection();
    Connection getConnection(String userName, String password);
    SQLConnectionPoolStat getStat();
}
