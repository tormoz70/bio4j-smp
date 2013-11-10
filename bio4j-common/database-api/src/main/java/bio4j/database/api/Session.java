package bio4j.database.api;

import java.sql.*;

public interface Session {
    String getConnectionString();
    Connection getConnection();
    void storeTransaction(String transactionName, Connection conn);
    Connection RestoreTransaction(String transactionName);
    void killTransaction(String transactionName);
    void KillTransactions();
}
