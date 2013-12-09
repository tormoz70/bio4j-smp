package ru.bio4j.smp.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import ru.bio4j.smp.common.types.Params;

public interface SQLCommand {
	boolean init(Connection conn, String sql, Params prms, int timeout);
    boolean init(Connection conn, String sql, Params prms);

	boolean openCursor(Params params);

	Params getParams();

    boolean cancel();

    boolean closeCursor();

	boolean next();

	boolean isActive();

	Connection getConnection();

	String getPreparedSQL();

	String getSQL();

	ResultSet getResultSet();

	Statement getStatement();

	Map<String, Field> getRow();

	Long getRowPos();

    Exception getLastError();

    boolean execSQL(Params params);

    void addBeforeEvent(SQLCommandBeforeEvent e);
    void addAfterEvent(SQLCommandAfterEvent e);
    void clearBeforeEvents();
    void clearAfterEvents();

    boolean isDBNull(String fieldName);
    boolean isDBNull(int fieldId);

    <T> T getValue(Class<T> type, String fieldName);
    <T> T getValue(Class<T> type, int fieldId);

    Object getValue(String fieldName);
    Object getValue(int fieldId);


}
