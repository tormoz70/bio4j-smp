package ru.bio4j.smp.database.api;

import ru.bio4j.smp.common.types.Params;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 17.12.13
 * Time: 0:26
 * To change this template use File | Settings | File Templates.
 */
public interface SQLStoredProc {
    boolean init(Connection conn, String storedProcName, Params params, int timeout);
    boolean init(Connection conn, String storedProcName, Params params);
    boolean execSQL(Params params);
    boolean execSQL();
}
