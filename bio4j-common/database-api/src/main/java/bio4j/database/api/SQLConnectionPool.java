package bio4j.database.api;

import java.sql.*;

public interface SQLConnectionPool {
    Connection getConnection();
    Connection getConnection(String userName, String password);
}
