package bio4j.database.direct.oracle.access.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.*;
import ru.bio4j.smp.common.types.DelegateSQLAction;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Converter;
import ru.bio4j.smp.common.utils.StringUtl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.common.utils.Utl;
import ru.bio4j.smp.database.api.*;

/**
 * Реализует 3 основных вида запроса Query, Exec, Scalar
 */
public class OraCommandImpl implements SQLCommand {
    private static final Logger LOG = LoggerFactory.getLogger(OraCommandImpl.class);

	private final Integer ciORAERRCODE_APP_ERR_START = 20000; // начало диапазона кодов ошибок приложения в Oracle
	private final Integer ciORAERRCODE_USER_BREAKED = 1013; // {"ORA-01013: пользователем запрошена отмена текущей операции"}
    private final Integer ciFetchedRowLimit = 10*10^6; // Максимальное кол-во записей, которое может вернуть запрос к БД (10 млн)
	private Params params = null;
	private int timeout = 60;
	private boolean isActive = false;
	private OracleResultSet resultSet = null;
	private OracleConnection connection = null;
	private OraclePreparedStatement preparedStatement = null;
	private Exception lastError = null;
    private StatementType statementType;

    private OraSQLWrapper sqlWrapper;
    private OraParamSetter paramSetter;
    private OraParamGetter paramGetter;

	private String sql = null;
	private String preparedSQL = null;
	private long currentFetchedRowPosition = 0L;
	private boolean closeConnectionOnClose = false;

    private ArrayList<SQLCommandBeforeEvent> beforeEvents = new ArrayList<SQLCommandBeforeEvent>();
    private ArrayList<SQLCommandAfterEvent> afterEvents = new ArrayList<SQLCommandAfterEvent>();

    public void addBeforeEvent(SQLCommandBeforeEvent e) {
        this.beforeEvents.add(e);
    }
    public void addAfterEvent(SQLCommandAfterEvent e) {
        this.afterEvents.add(e);
    }
    public void clearBeforeEvents() {
        this.beforeEvents.clear();
    }
    public void clearAfterEvents() {
        this.afterEvents.clear();
    }

    public OraCommandImpl() {
	}

	@Override
	public boolean init(StatementType statementType, Connection conn, String sql, Params params, int timeout) {
        this.statementType = statementType;
		this.connection = (OracleConnection)conn;
		this.lastError = null;
		this.timeout = timeout;
		this.sql = sql;
		this.params = params;
		return this.prepareStatement();
	}
    @Override
    public boolean init(StatementType statementType, Connection conn, String sql, Params params) {
        return this.init(statementType, conn, sql, params, 60);
    }

	private boolean prepareStatement() {
        try {
            this.preparedSQL = OraUtils.detectSQLParamsAuto(this.sql, this.connection);
            this.preparedSQL = (this.sqlWrapper != null) ? this.sqlWrapper.prepare(this.preparedSQL) : this.preparedSQL;
            if(this.statementType == StatementType.EXEC)
                this.preparedStatement = (OracleCallableStatement)this.connection.prepareCall(this.preparedSQL);
            else
		        this.preparedStatement = (OraclePreparedStatement)this.connection.prepareStatement(this.preparedSQL, ResultSet.TYPE_FORWARD_ONLY);
            ParameterMetaData pmd = ((OracleCallableStatement)this.preparedStatement).getParameterMetaData();
            int pcnt = pmd.getParameterCount();
            for (int i = 1; i <= pcnt; i++) {
                LOG.debug("{ i:" + i);
                //LOG.debug("  ParameterClassName:"+pmd.getParameterClassName(i));
                //LOG.debug("  ParameterTypeName:"+pmd.getParameterTypeName(i));
                //LOG.debug("  ParameterType:"+pmd.getParameterType(i));
                //LOG.debug("  ParameterMode:"+pmd.getParameterMode(i));
                //LOG.debug("  Precision:"+pmd.getPrecision(i));
                //LOG.debug("  Scale:"+pmd.getScale(i));
                LOG.debug("}");
            }
            this.preparedStatement.setQueryTimeout(this.timeout);
            return true;
        } catch (SQLException ex) {
            this.lastError = ex;
            LOG.error("Error!!!", ex);
            return false;
        }
	}
	
	private boolean doBeforeStatement(Params params){
        boolean locCancel = false;
        if(this.beforeEvents.size() > 0) {
            for(SQLCommandBeforeEvent e : this.beforeEvents){
                SQLCommandBeforeEventAttrs attrs = new SQLCommandBeforeEventAttrs(false, params);
                e.handle(this, attrs);
                locCancel = locCancel || attrs.getCancel();
            }
        }
        if(locCancel)
            this.lastError = new SQLException("Command has been canceled!");
        return !locCancel;
	}

    private void doAfterStatement(SQLCommandAfterEventAttrs attrs){
        if(this.afterEvents.size() > 0) {
            for(SQLCommandAfterEvent e : this.afterEvents)
                e.handle(this, attrs);
        }
    }

    private void resetCommand() {
        this.lastError = null;
        if (this.isActive())
            this.closeCursor();
        this.currentFetchedRowPosition = 0L;
    }


    /**
     * Присваивает значения входящим параметрам
     */
    private void setParamsToStatement() throws SQLException {
        if(this.paramSetter != null)
            this.paramSetter.setParamsToStatement(this.preparedSQL, this.preparedStatement, this.params);
    }

    private void getBackOutParams() throws SQLException {
        if((this.paramGetter != null) && (this.preparedStatement instanceof OracleCallableStatement))
            this.paramGetter.getParamsFromStatement((OracleCallableStatement)this.preparedStatement, this.params);
    }

    public void setParamSetter(OraParamSetter paramSetter) {
        this.paramSetter = paramSetter;
    }

    public void setParamGetter(OraParamGetter paramGetter) {
        this.paramGetter = paramGetter;
    }

    private <T> Object processStatement(Params params, DelegateSQLAction<T> action) {
        Object result = false;
        try {
            try {
                this.resetCommand(); // Сбрасываем состояние

                this.params = this.params.merge(params, true); // Объединяем параметры

                if(!this.doBeforeStatement(this.params)) // Обрабатываем события
                    return result;

                this.setParamsToStatement(); // Применяем параметры

                if (action != null)
                    result = action.execute(); // Выполняем команду

                this.getBackOutParams(); // Вытаскиваем OUT-параметры

            } catch (Exception e) {
                this.lastError = e;
                LOG.error("Error on exec sql : [" + this.preparedSQL + "]", e);
//                for (int i = 1; i <= this.preparedStatement.getParameterMetaData().getParameterCount(); i++) {
//                    LOG.debug("1: "+this.preparedStatement.getParameterMetaData().getParameterType(i));
//                }
            }
            this.doAfterStatement(SQLCommandAfterEventAttrs.build( // Обрабатываем события
                    this.params, this.resultSet, this.lastError
            ));
        } finally {
            return result; // Возвращаем результат
        }
    }

	@Override
	public boolean openCursor(Params params) {
        if(!Utl.arrayContains(new StatementType[]{StatementType.QUERY, StatementType.SCALAR}, this.statementType)) {
            this.lastError = new IllegalArgumentException("Проинициализирован как " + this.statementType + ". Не возможно открыть как Cursor!");
            return false;
        }
        return (boolean)this.processStatement(params, new DelegateSQLAction<Boolean>() {
            @Override
            public Boolean execute() throws SQLException {
                final OraCommandImpl self = OraCommandImpl.this;
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

    @Override
	public boolean execSQL(Params params) {
        if(!Utl.arrayContains(new StatementType[]{StatementType.EXEC}, this.statementType)) {
            this.lastError = new IllegalArgumentException("Проинициализирован как " + this.statementType + ". Не возможно выполнить как Exec!");
            return false;
        }
        return (boolean)this.processStatement(params, new DelegateSQLAction<Boolean>() {
            @Override
            public Boolean execute() throws SQLException {
                final OraCommandImpl self = OraCommandImpl.this;
                ((OracleCallableStatement)self.preparedStatement).executeUpdate();
                return true;
            }
        });
	}

    @Override
    public boolean execSQL() {
        return this.execSQL(null);
    }

	@SuppressWarnings("unchecked")
    @Override
	public <T> T execScalar(Class<T> clazz, Params params){
        return (T)this.processStatement(params, new DelegateSQLAction<T>() {
            @Override
            public T execute() throws SQLException {
                final OraCommandImpl self = OraCommandImpl.this;
                try(OracleResultSet resultSet = (OracleResultSet)self.preparedStatement.executeQuery()) {
                    if(resultSet.next())
                        return (T)resultSet.getObject(1);
                }
                return null;
            }
        });
	}

    @Override
    public <T> T execScalar(Class<T> clazz){
        return this.execScalar(clazz, null);
    }

	@Override
	public boolean cancel() {
        if (this.lastError != null)
            return false;
        final Statement stmnt = this.getStatement();
        if(stmnt != null) {
            try {
                stmnt.cancel();
                return true;
            } catch (SQLException ex) {
                this.lastError = ex;
                return false;
            }
        }
        return true;
	}

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
        final ResultSet rsltSet = this.getResultSet();
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
	public Params getParams() {
		return this.params;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public String getPreparedSQL() {
		return this.preparedSQL;
	}

	@Override
	public String getSQL() {
		return this.sql;
	}

	@Override
	public ResultSet getResultSet() {
		return this.resultSet;
	}

	@Override
	public Statement getStatement() {
		return this.preparedStatement;
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

    public void setSqlWrapper(OraSQLWrapper sqlWrapper) {
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
