package ru.bio4j.smp.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.ConvertValueException;

public interface SQLCursor extends SQLCommand, AutoCloseable {
    /**
     * Инициализировать курсор
     * @param conn - соединение
     * @param sql - предложение
     * @param prms - параметры
     * @param timeout - время ожидания ответа в сек (по умолчанию 60 сек)
     * @return ссылка на этот же курсор
     * @throws SQLException
     */
    SQLCursor init(Connection conn, String sql, Params prms, int timeout) throws SQLException;
    /**
     * Инициализировать курсор
     * @param conn - соединение
     * @param sql - предложение
     * @param prms - параметры
     * @return ссылка на этот же курсор
     * @throws SQLException
     */
    SQLCursor init(Connection conn, String sql, Params prms) throws SQLException;

    SQLCursor open(Params params) throws SQLException;
    SQLCursor open() throws SQLException;

	boolean next() throws SQLException;

	boolean isActive();

	String getSQL();

	Map<String, Field> getRow();

	Long getRowPos();

    boolean isDBNull(String fieldName);
    boolean isDBNull(int fieldId);

    <T> T getValue(Class<T> type, String fieldName) throws SQLException;
    <T> T getValue(Class<T> type, int fieldId) throws SQLException;

    Object getValue(String fieldName);
    Object getValue(int fieldId);
}
