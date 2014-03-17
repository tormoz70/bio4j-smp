package ru.bio4j.smp.database.direct.oracle.access.impl;

import oracle.jdbc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.common.types.DelegateSQLAction;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Converter;
import ru.bio4j.smp.common.utils.StringUtl;
import ru.bio4j.smp.database.api.*;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализует 3 основных вида запроса Query, Exec, Scalar
 */
public class OraCursor extends OraCommand<SQLCursor> implements SQLCursor {
    private static final Logger LOG = LoggerFactory.getLogger(OraCursor.class);

    public static final int FETCH_ROW_LIMIT = 10*10^6; // Максимальное кол-во записей, которое может вернуть запрос к БД (10 млн)

	private boolean isActive = false;

	private String sql = null;
	private long currentFetchedRowPosition = 0L;

    public OraCursor() {
	}

	@Override
	public SQLCursor init(Connection conn, String sql, Params params, int timeout) throws SQLException {
        this.sql = sql;
		return super.init(conn, params, timeout);
	}

    @Override
    public SQLCursor init(Connection conn, String sql, Params params) throws SQLException {
        return this.init(conn, sql, params, 60);
    }

    @Override
	protected void prepareStatement() throws SQLException {
        this.preparedSQL = (this.sqlWrapper != null) ? this.sqlWrapper.prepare(this.sql) : this.sql;
        this.preparedStatement = (OraclePreparedStatement)this.connection.prepareStatement(this.preparedSQL, ResultSet.TYPE_FORWARD_ONLY);
        this.preparedStatement.setQueryTimeout(this.timeout);
	}


    @Override
    protected void resetCommand() throws SQLException {
        super.resetCommand();
        if (this.isActive()){
            try {
                this.close();
            } catch (Exception e) {}
        }
        this.currentFetchedRowPosition = 0L;
    }

	@Override
	public SQLCursor open(Params params) throws SQLException {
        return (SQLCursor)this.processStatement(params, new DelegateSQLAction() {
            @Override
            public void execute() throws SQLException {
                final OraCursor self = OraCursor.this;
                self.resultSet = (OracleResultSet)self.preparedStatement.executeQuery();
                self.isActive = true;
            }
        });
	}

    @Override
    public SQLCursor open() throws SQLException {
        return this.open(null);
    }


//	@SuppressWarnings("unchecked")
//    @Override
//	public <T> T execScalar(Class<T> clazz, Params params){
//        return (T)this.processStatement(params, new DelegateSQLAction<T>() {
//            @Override
//            public T execute() throws SQLException {
//                final OraCursor self = OraCursor.this;
//                try(OracleResultSet resultSet = (OracleResultSet)self.preparedStatement.executeQuery()) {
//                    if(resultSet.next())
//                        return (T)resultSet.getObject(1);
//                }
//                return null;
//            }
//        });
//	}
//
//    @Override
//    public <T> T execScalar(Class<T> clazz){
//        return this.execScalar(clazz, null);
//    }

    private static String readClob(Clob clob) throws SQLException {
        String result = null;
        Reader is = clob.getCharacterStream();
//        InputStreamReader is = new InputStreamReader(clob.getAsciiStream(), Charset.defaultCharset());
        StringBuffer sb = new StringBuffer();
        int length = (int) clob.length();
        if (length > 0) {
            char[] buffer = new char[length];
            try {
                while (is.read(buffer) != -1)
                    sb.append(buffer);
                result = new String(sb);
            } catch (IOException e) {
                new SQLException(e);
            }
        }
        return result;
    }

    private Map<String, Field> row;
    private List<Object> rowValues;

    private void readRow(ResultSet resultSet) throws SQLException {
        if(this.row == null) {
            OracleResultSetMetaData metadata = (OracleResultSetMetaData)resultSet.getMetaData();
            this.rowValues = new ArrayList<Object>(metadata.getColumnCount());
            for (int i = 0; i < metadata.getColumnCount(); i++)
                this.rowValues.add(null);

            this.row = new HashMap<>();
            for (int i = 1; i < metadata.getColumnCount(); i++) {
                Class<?> type = null;
                try {
                    type = getClass().getClassLoader().loadClass(metadata.getColumnClassName(i));
                } catch (ClassNotFoundException ex) {
                    throw new SQLException(ex);
                }
                String fieldName =  metadata.getColumnName(i);
                int sqlType = metadata.getColumnType(i);
                Field field = new FieldImpl(type, i, fieldName, sqlType);
                this.row.put(fieldName, field);
            }
        }
        for (Field field : this.row.values()) {
            int valueIndex = field.getId() - 1;
            Object value;
            if(field.getType() == oracle.jdbc.OracleClob.class){
                value = readClob(resultSet.getClob(field.getId()));
            } else if(field.getType() == oracle.jdbc.OracleBlob.class){
                value = resultSet.getBytes(field.getId());
            } else
                value = resultSet.getObject(field.getId());
            this.rowValues.set(valueIndex, value);
        }
    }

	@Override
	public boolean next() throws SQLException {
        boolean rslt = false;
		if (this.resultSet != null) {
            rslt = this.resultSet.next();
            if(rslt)
               this.readRow(this.resultSet);

        }
		return rslt;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public String getSQL() {
		return this.sql;
	}

	@Override
	public Map<String, Field> getRow() {
		return this.row;
	}

	@Override
	public Long getRowPos() {
		return this.currentFetchedRowPosition;
	}

    public void setSqlWrapper(SQLWrapper sqlWrapper) {
        this.sqlWrapper = sqlWrapper;
    }

    @Override
    public boolean isDBNull(String fieldName) {
        return false;
    }
    @Override
    public boolean isDBNull(int fieldId) {
        return false;
    }

    private final String EXMSG_FieldNotFound = "Поле %s не найдено!";
    private final String EXMSG_IndexOutOfBounds = "Индекс [%d] за пределами диапазона!";
    private final String EXMSG_ParamIsNull = "Обязательный параметр [%s] пуст!";

    @Override
    public <T> T getValue(Class<T> type, int fieldId) throws SQLException {
        if((fieldId > 0) && (fieldId <= this.rowValues.size())) {
            try {
                return Converter.toType(this.rowValues.get(fieldId - 1), type);
            } catch (ConvertValueException e) {
                throw new SQLException(e);
            }
        } else
            throw new IllegalArgumentException(String.format(EXMSG_IndexOutOfBounds, fieldId));
    }

    @Override
    public <T> T getValue(Class<T> type, String fieldName) throws SQLException {
        if(StringUtl.isNullOrEmpty(fieldName))
            throw new IllegalArgumentException(String.format(EXMSG_ParamIsNull, "fieldName"));

        Field fld = this.row.get(fieldName.toUpperCase());
        if(fld != null)
            return getValue(type, fld.getId());
        else
            throw new IllegalArgumentException(String.format(EXMSG_FieldNotFound, fieldName));
    }

    @Override
    public Object getValue(int fieldId) {
        if((fieldId > 0) && (fieldId <= this.rowValues.size()))
            return this.rowValues.get(fieldId - 1);
        else
            throw new IllegalArgumentException(String.format(EXMSG_IndexOutOfBounds, fieldId));
    }

    @Override
    public Object getValue(String fieldName) {
        if(StringUtl.isNullOrEmpty(fieldName))
            throw new IllegalArgumentException(String.format(EXMSG_ParamIsNull, "fieldName"));

        Field fld = this.row.get(fieldName.toUpperCase());
        if(fld != null)
            return getValue(fld.getId());
        else
            throw new IllegalArgumentException(String.format(EXMSG_FieldNotFound, fieldName));

    }

    @Override
    public void close() throws Exception {
        this.isActive = false;
        this.row = null;
        this.cancel();
        final Statement stmnt = this.getStatement();
        if(stmnt != null)
            stmnt.close();

        final ResultSet rsltSet = this.resultSet;
        if(rsltSet != null)
            rsltSet.close();

    }
}
