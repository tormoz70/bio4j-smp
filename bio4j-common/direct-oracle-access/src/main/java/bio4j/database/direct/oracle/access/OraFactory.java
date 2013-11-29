package bio4j.database.direct.oracle.access;

import bio4j.database.api.*;

public class OraFactory {
	public static SQLCommand CreateSQLCommand(){
		return new OraCommandImpl();
	}

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config) {
        return OraConnectionPoolImpl.create(poolName, config);
    }


}
