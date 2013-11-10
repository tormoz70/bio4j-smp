package bio4j.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import bio4j.common.types.Params;

public interface SQLCmd {
	Boolean init(Connection conn, String sql, Params prms, Long timeout);

	Boolean open();

	Params getParams();

	void cancel();

	void close();

	Boolean next();

	Boolean isActive();

	Connection getConnection();

	String getPreparedSQL();

	String getSQL();

	ResultSet getResultSet();

	Statement getStatement();

	Map<String, ?> getRow();

	Long getRowPos();

	SQLException getLastError();
}
