package bio4j.database.direct.oracle.access;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import bio4j.common.types.DelegateSQLAction;
import bio4j.common.types.Params;
import bio4j.database.api.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private OraSQLWrapper sqlWrapper;
    private OraParamSetter paramSetter;
    private OraParamGetter paramGetter;
    private OraMetaDataReader metaDataReader;
    private OraDataReader dataReader;

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
	public Boolean init(Connection conn, String sql, Params params, int timeout) {
		Boolean result = true;
		this.connection = (OracleConnection)conn;
		this.lastError = null;
		this.timeout = timeout;
		this.sql = sql;
		this.params = params;
		return this.prepareStatement();
	}
    @Override
    public Boolean init(Connection conn, String sql, Params params) {
        return this.init(conn, sql, params, 60);
    }

	private Boolean prepareStatement() {
        try {
            this.preparedSQL = OraUtils.detectSQLParamsAuto(this.sql, this.connection);
            this.preparedSQL = (this.sqlWrapper != null) ? this.sqlWrapper.prepare(this.preparedSQL) : this.preparedSQL;
		    this.preparedStatement = (OraclePreparedStatement)this.connection.prepareStatement(this.preparedSQL, ResultSet.TYPE_FORWARD_ONLY);
            this.preparedStatement.setQueryTimeout(this.timeout);
            return true;
        } catch (SQLException ex) {
            this.lastError = ex;
            LOG.error("Error!!!", ex);
            return false;
        }
	}
	
	private Boolean doBeforeStatement(Params params){
        Boolean locCancel = false;
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
        if(this.paramGetter != null)
            this.paramGetter.getParamsFromStatement(this.preparedStatement, this.params);
    }

    public void setParamSetter(OraParamSetter paramSetter) {
        this.paramSetter = paramSetter;
    }

    public void setParamGetter(OraParamGetter paramGetter) {
        this.paramGetter = paramGetter;
    }

    public void setMetaDataReader(OraMetaDataReader metaDataReader) {
        this.metaDataReader = metaDataReader;
    }

    public void setDataReader(OraDataReader dataReader) {
        this.dataReader = dataReader;
    }

    private enum StatementType {
        QUERY,
        EXEC,
        SCALAR
    }

    private <T> Object processStatement(StatementType statementType, Params params, DelegateSQLAction<T> action) {
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

            } catch (SQLException e) {
                this.lastError = e;
                LOG.debug("Error on exec sql : [" + this.preparedSQL + "]", e);
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
	public Boolean openCursor(Params params) {
        return (Boolean)this.processStatement(StatementType.QUERY, params, new DelegateSQLAction<Boolean>() {
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
	public Boolean execSQL(Params params) {
        return (Boolean)this.processStatement(StatementType.EXEC, params, new DelegateSQLAction<Boolean>() {
            @Override
            public Boolean execute() throws SQLException {
                final OraCommandImpl self = OraCommandImpl.this;
                self.preparedStatement.execute(self.getPreparedSQL());
                return true;
            }
        });
	}

	@SuppressWarnings("unchecked")
	public <T> T execScalar(Class<T> clazz, Params params){
        return (T)this.processStatement(StatementType.SCALAR, params, new DelegateSQLAction<T>() {
            @Override
            public T execute() throws SQLException {
                final OraCommandImpl self = OraCommandImpl.this;
                try(OracleResultSet resultSet = (OracleResultSet)self.preparedStatement.executeQuery(self.getPreparedSQL());) {
                    if(resultSet.next())
                        return (T)resultSet.getObject(1);
                }
                return null;
            }
        });
	}
	
	@Override
	public Boolean cancel() {
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
	public Boolean closeCursor() {
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

	@Override
	public Boolean next() {
        Boolean rslt = (this.lastError == null);
        if(!rslt) return false;
		if (this.resultSet != null) {
            try {
                rslt = this.resultSet.next();
                if(rslt){
                    if(this.row == null)
                        this.row = this.metaDataReader.read(this.resultSet);
                    this.dataReader.read(this.resultSet, this.row);
                }

            } catch (Exception ex) {
                LOG.error("Error!!!", ex);
                this.lastError = ex;
                rslt = false;
            }
        }
		return rslt;
	}

	@Override
	public Boolean isActive() {
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

}
