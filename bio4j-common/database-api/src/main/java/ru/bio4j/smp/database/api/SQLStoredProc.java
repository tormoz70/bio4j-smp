package ru.bio4j.smp.database.api;

import ru.bio4j.smp.common.types.Params;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 17.12.13
 * Time: 0:26
 * To change this template use File | Settings | File Templates.
 */
public interface SQLStoredProc extends SQLCommand {
    SQLStoredProc init(Connection conn, String storedProcName, Params params, int timeout) throws SQLException;
    SQLStoredProc init(Connection conn, String storedProcName, Params params) throws SQLException;
    void execSQL(Params params) throws SQLException;
    void execSQL() throws SQLException;
}
