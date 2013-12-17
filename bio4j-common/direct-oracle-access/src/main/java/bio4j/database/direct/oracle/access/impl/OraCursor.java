package bio4j.database.direct.oracle.access.impl;

import oracle.jdbc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.common.types.DelegateSQLAction;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Converter;
import ru.bio4j.smp.common.utils.StringUtl;
import ru.bio4j.smp.database.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализует 3 основных вида запроса Query, Exec, Scalar
 */
public class OraCursor extends OraCommand implements SQLCursor {
    private static final Logger LOG = LoggerFactory.getLogger(OraCursor.class);

    public static final int FETCH_ROW_LIMIT = 10*10^6; // Максимальное кол-во записей, которое может вернуть запрос к БД (10 млн)

	private boolean isActive = false;

	private String sql = null;
	private long currentFetchedRowPosition = 0L;

    public OraCursor() {
	}

	@Override
	public boolean init(Connection conn, String sql, Params params, int timeout) {
        this.sql = sql;
		return super.init(conn, params, timeout) && this.prepareStatement();
	}

    @Override
    public boolean init(Connection conn, String sql, Params params) {
        return this.init(conn, sql, params, 60);
    }

    @Override
	protected boolean prepareStatement() {
        try {
            this.preparedSQL = (this.sqlWrapper != null) ? this.sqlWrapper.prepare(this.sql) : this.sql;
            this.preparedStatement = (OraclePreparedStatement)this.connection.prepareStatement(this.preparedSQL, ResultSet.TYPE_FORWARD_ONLY);
            this.preparedStatement.setQueryTimeout(this.timeout);
            return true;
        } catch (SQLException ex) {
            this.lastError = ex;
            LOG.error("Error!!!", ex);
            return false;
        }
	}


    @Override
    protected void resetCommand() {
        super.resetCommand();
        if (this.isActive())
            this.closeCursor();
        this.currentFetchedRowPosition = 0L;
    }

	@Override
	public boolean openCursor(Params params) {
        return (boolean)this.processStatement(params, new DelegateSQLAction<Boolean>() {
            @Override
            public Boolean execute() throws SQLException {
                final OraCursor self = OraCursor.this;
                self.resultSet = (OracleResultSet)self.preparedStatement.executeQuery();
                self.isActive = true;
                return true;
            }
        });
	}

    @Override
    public boolean openCursor() {
        return this.openCursor(null);
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


	@Override
	public boolean closeCursor() {
		this.isActive = false;
        this.cancel();
        if (this.lastError != null)
            return false;
        final Statement stmnt = this.getStatement();
        if(stmnt != null) {
            try {
                stmnt.close();
                return true;
            } catch (SQLException ex) {
                this.lastError = ex;
                return false;
            }
        }
        final ResultSet rsltSet = this.resultSet;
        if(rsltSet != null) {
            try {
                rsltSet.close();
                return true;
            } catch (SQLException ex) {
                this.lastError = ex;
                return false;
            }
        }
        this.row = null;
        return true;
	}

    private Map<String, Field> row;
    private List<Object> rowValues;

    private boolean readRow(ResultSet resultSet) throws SQLException {
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
                    this.lastError = ex;
                    LOG.error("Error!", ex);
                    return false;
                }
                String fieldName =  metadata.getColumnName(i);
                int fieldType = metadata.getColumnType(i);
                Field field = new FieldImpl(type, i, fieldName, fieldType);
                this.row.put(fieldName, field);
            }
        }
        for (Field field : this.row.values()) {
            int valueIndex = field.getId() - 1;
            this.rowValues.set(valueIndex, resultSet.getObject(field.getId()));
        }
        return true;
    }

	@Override
	public boolean next() {
        boolean rslt = (this.lastError == null);
        if(!rslt) return false;
		if (this.resultSet != null) {
            try {
                rslt = this.resultSet.next();
                if(rslt)
                   this.readRow(this.resultSet);

            } catch (Exception ex) {
                LOG.error("Error!!!", ex);
                this.lastError = ex;
                rslt = false;
            }
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

	@Override
	public Exception getLastError() {
		return this.lastError;
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
    public <T> T getValue(Class<T> type, int fieldId) {
        if((fieldId > 0) && (fieldId <= this.rowValues.size())) {
            try {
                return Converter.toType(this.rowValues.get(fieldId - 1), type);
            } catch (ConvertValueException ex) {
                this.lastError = ex;
                LOG.error("Error!", ex);
                return null;
            }
        } else {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_IndexOutOfBounds, fieldId));
            return null;
        }
    }

    @Override
    public <T> T getValue(Class<T> type, String fieldName) {
        if(StringUtl.isNullOrEmpty(fieldName)) {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_ParamIsNull, "fieldName"));
            return null;
        }
        Field fld = this.row.get(fieldName.toUpperCase());
        if(fld != null)
            return getValue(type, fld.getId());
        else {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_FieldNotFound, fieldName));
            return null;
        }
    }

    @Override
    public Object getValue(int fieldId) {
        if((fieldId > 0) && (fieldId <= this.rowValues.size()))
            return this.rowValues.get(fieldId - 1);
        else {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_IndexOutOfBounds, fieldId));
            return null;
        }
    }

    @Override
    public Object getValue(String fieldName) {
        if(StringUtl.isNullOrEmpty(fieldName)) {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_ParamIsNull, "fieldName"));
            return null;
        }
        Field fld = this.row.get(fieldName.toUpperCase());
        if(fld != null)
            return getValue(fld.getId());
        else {
            this.lastError = new IllegalArgumentException(String.format(EXMSG_FieldNotFound, fieldName));
            return null;
        }
    }

}
