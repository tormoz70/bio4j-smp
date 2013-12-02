package bio4j.database.direct.oracle.access;

import bio4j.database.api.*;

public class OraFactory {
	public static SQLCommand CreateSQLCommand(){
        OraCommandImpl cmd = new OraCommandImpl();
        cmd.setParamSetter(new OraParamSetter(cmd));
        cmd.setParamGetter(new OraParamGetter(cmd));
        cmd.setSqlWrapper(new OraSQLWrapper(cmd));
        cmd.setMetaDataReader(new OraMetaDataReader(cmd));
        cmd.setDataReader(new OraDataReader(cmd));
		return cmd;
	}

    public static SQLConnectionPool CreateSQLConnectionPool(String poolName, SQLConnectionPoolConfig config) {
        return OraConnectionPoolImpl.create(poolName, config);
    }


}
