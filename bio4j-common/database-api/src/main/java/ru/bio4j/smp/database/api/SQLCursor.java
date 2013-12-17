package ru.bio4j.smp.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import ru.bio4j.smp.common.types.Params;

public interface SQLCursor extends SQLCommand {
	boolean init(Connection conn, String sql, Params prms, int timeout);
    boolean init(Connection conn, String sql, Params prms);

	boolean openCursor(Params params);
    boolean openCursor();

    boolean closeCursor();

	boolean next();

	boolean isActive();

	String getSQL();

	Map<String, Field> getRow();

	Long getRowPos();

//    <T> T execScalar(Class<T> clazz, Params params);
//    <T> T execScalar(Class<T> clazz);

    boolean isDBNull(String fieldName);
    boolean isDBNull(int fieldId);

    <T> T getValue(Class<T> type, String fieldName);
    <T> T getValue(Class<T> type, int fieldId);

    Object getValue(String fieldName);
    Object getValue(int fieldId);
}
