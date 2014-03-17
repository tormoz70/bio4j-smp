package ru.bio4j.smp.database.direct.oracle.access;

import ru.bio4j.smp.database.api.*;
import ru.bio4j.smp.database.direct.oracle.access.impl.*;

import java.sql.SQLException;

public class OraFactory {
	public static SQLCursor CreateSQLCursor(){
        OraCursor cmd = new OraCursor();
        cmd.setParamSetter(new OraSelectableParamSetter(cmd));
		return cmd;
	}

    public static SQLStoredProc CreateSQLStoredProc(){
        OraStoredProc cmd = new OraStoredProc();
        cmd.setParamSetter(new OraCallableParamSetter(cmd));
        cmd.setParamGetter(new OraCallableParamGetter(cmd));
        return cmd;
    }

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config)  throws SQLException {
        return OraConnectionPool.create(poolName, config);
    }


}
