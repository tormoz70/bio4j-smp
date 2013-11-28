package bio4j.database.direct.oracle.access;

import bio4j.database.api.*;

public class SQLFactory {
	public static SQLCommand CreateSQLCommand(){
		return new SQLCommandImpl();
	}

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config) {
        return SQLConnectionPoolImpl.create(poolName, config);
    }


}
