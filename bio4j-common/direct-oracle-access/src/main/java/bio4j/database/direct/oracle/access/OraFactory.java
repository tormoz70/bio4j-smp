package bio4j.database.direct.oracle.access;

import bio4j.database.direct.oracle.access.impl.*;
import ru.bio4j.smp.database.api.*;

import java.sql.SQLException;

public class OraFactory {
	public static SQLCursor CreateSQLCursor(){
        OraCursor cmd = new OraCursor();
        cmd.setParamSetter(new OraParamSetter(cmd));
		return cmd;
	}

    public static SQLStoredProc CreateSQLStoredProc(){
        OraStoredProc cmd = new OraStoredProc();
        cmd.setParamSetter(new OraParamSetter(cmd));
        cmd.setParamGetter(new OraParamGetter(cmd));
        return cmd;
    }

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config)  throws SQLException {
        return OraConnectionPool.create(poolName, config);
    }


}
