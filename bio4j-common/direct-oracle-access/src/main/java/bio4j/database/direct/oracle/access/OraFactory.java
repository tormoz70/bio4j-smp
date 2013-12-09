package bio4j.database.direct.oracle.access;

import bio4j.database.direct.oracle.access.impl.*;
import ru.bio4j.smp.database.api.SQLCommand;
import ru.bio4j.smp.database.api.SQLConnectionPool;
import ru.bio4j.smp.database.api.SQLConnectionPoolConfig;

import java.sql.SQLException;

public class OraFactory {
	public static SQLCommand CreateSQLCommand(){
        OraCommandImpl cmd = new OraCommandImpl();
        cmd.setParamSetter(new OraParamSetter(cmd));
        cmd.setParamGetter(new OraParamGetter(cmd));
        cmd.setSqlWrapper(new OraSQLWrapper(cmd));
		return cmd;
	}

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config)  throws SQLException {
        return OraConnectionPoolImpl.create(poolName, config);
    }


}
