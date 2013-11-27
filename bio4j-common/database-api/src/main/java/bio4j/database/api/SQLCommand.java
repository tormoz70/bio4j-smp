package bio4j.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import bio4j.common.types.Params;

public interface SQLCommand {
	Boolean init(Connection conn, String sql, Params prms, Long timeout);

	Boolean openCursor(Params params);

	Params getParams();

    Boolean cancel();

    Boolean closeCursor();

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

    Boolean execSQL(Params params);

    void addBeforeEvent(SQLCommandBeforeEvent e);
    void addAfterEvent(SQLCommandAfterEvent e);
    void clearBeforeEvents();
    void clearAfterEvents();
}
