package bio4j.database.direct.oracle.access;

import bio4j.database.api.SQLCmd;

public class SQLFactory {
	public static SQLCmd CreateSQLCmd(){
		return new SQLCmdImpl();
	}
}
