package bio4j.database.direct.oracle.access;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bio4j.common.types.Params;
import bio4j.database.api.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLCommandImpl implements SQLCommand {
    private static final Logger LOG = LoggerFactory.getLogger(SQLCommandImpl.class);

	private final Integer ciORAERRCODE_APP_ERR_START = 20000; // начало диапазона кодов ошибок приложения в Oracle
	private final Integer ciORAERRCODE_USER_BREAKED = 1013; // {"ORA-01013: пользователем запрошена отмена текущей операции"}
    private final Integer ciFetchedRowLimit = 10000000; // Максимальное кол-во записей, которое может вернуть запрос к БД
	private Params params = null;
	private Long timeout = 60000L;
	private Boolean _isActive;
	private OracleResultSet resultSet = null;
	private OracleConnection connection = null;
	private OraclePreparedStatement preparedStatement = null;
	private SQLException lastError = null;

    private SQLWrapper sqlWrapper;

	private String sql = null;
	private String preparedSQL = null;
	private HashMap<String, ?> rowValues = null;
	private Long currentFetchedRowPosition = 0L;
	private Boolean closeConnectionOnClose = false;

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

    public SQLCommandImpl() {
	}

	@Override
	public Boolean init(Connection conn, String sql, Params params, Long timeout) {
		Boolean result = true;
		this.connection = (OracleConnection)conn;
		this.lastError = null;
		this.timeout = timeout;
		this.sql = sql;
		this.params = params;
		return this.prepareStatment();
	}

	private Boolean prepareStatment() {
        this.preparedSQL = (this.sqlWrapper != null) ? this.sqlWrapper.prepare(this.sql) : this.sql;
        try {
		    this.preparedStatement = (OraclePreparedStatement)this.connection.prepareStatement(this.preparedSQL, ResultSet.TYPE_FORWARD_ONLY);
            return true;
        } catch (SQLException ex) {
            this.lastError = ex;
            LOG.error("Error!!!", ex);
            return false;
        }
	}
	
	private Boolean doBeforeStatment(Params params){
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

    private void doAfterStatment(SQLCommandAfterEventAttrs attrs){
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

	@Override
	public Boolean openCursor(Params params) {
        try {
            try {
                this.resetCommand();

                this.params = this.params.merge(params, true);

                if(!this.doBeforeStatment(this.params))
                    return false;

                this.setParamsToStatment(this.params);
                this.resultSet = (OracleResultSet)this.getStatement().executeQuery(this.getPreparedSQL());
                this._isActive = true;
            } catch (SQLException e) {
                this.lastError = e;
            }
            this.doAfterStatment(SQLCommandAfterEventAttrs.build(
                this.params, this.resultSet, this.lastError
            ));
        } finally {
            return (this.lastError == null);
        }
	}

    @Override
	public Boolean execSQL(Params params) {
        try {
            try {
                this.resetCommand();

                this.params = this.params.merge(params, true);

                if(!this.doBeforeStatment(this.params))
                    return false;

                this.getStatement().execute(this.getPreparedSQL());
                this.getStatement().close();
            } catch (SQLException e) {
                this.lastError = e;
            }
            this.doAfterStatment(SQLCommandAfterEventAttrs.build(
                    this.params, this.lastError
            ));
        } finally {
            return (this.lastError == null);
        }
	}

	@SuppressWarnings("unchecked")
	public <T> T execScalar(Class<T> clazz, Params params){
        T result = null;
        try {
            try {
                this.resetCommand();

                this.params = this.params.merge(params, true);

                if(!this.doBeforeStatment(this.params))
                    return null;

                try {
                    try(OracleResultSet resultSet = (OracleResultSet)this.getStatement().executeQuery(this.getPreparedSQL());) {
                        if(resultSet.next())
                            result = (T)resultSet.getObject(1);
                    }
                }finally {
                    this.getStatement().close();
                }
            } catch (SQLException e) {
                this.lastError = e;
            }
            this.doAfterStatment(SQLCommandAfterEventAttrs.build(
                    this.params, this.lastError
            ));
        } finally {
            return result;
        }
	}
	
	private void setParamsToStatment(Params params) {
		
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
		this._isActive = false;
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
        return true;
	}

	@Override
	public Boolean next() {
        Boolean rslt = (this.lastError == null);
        if(!rslt) return false;
		if (this.resultSet != null) {
            try {
                rslt = this.resultSet.next();
            } catch (SQLException ex) {
                this.lastError = ex;
                rslt = false;
            }
        }
		return rslt;
	}

	@Override
	public Boolean isActive() {
		return this._isActive;
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
	public Map<String, ?> getRow() {
		return this.rowValues;
	}

	@Override
	public Long getRowPos() {
		return this.currentFetchedRowPosition;
	}

	@Override
	public SQLException getLastError() {
		return this.lastError;
	}

    public void setSqlWrapper(SQLWrapper sqlWrapper) {
        this.sqlWrapper = sqlWrapper;
    }
}
