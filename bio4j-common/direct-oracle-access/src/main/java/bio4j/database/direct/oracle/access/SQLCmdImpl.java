package bio4j.database.direct.oracle.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import bio4j.common.types.Params;
import bio4j.database.api.SQLCmd;

public class SQLCmdImpl implements SQLCmd {

	protected final Integer ciORAERRCODE_APP_ERR_START = 20000; // начало диапазона кодов ошибок приложения в Oracle
	protected final Integer ciORAERRCODE_USER_BREAKED = 1013; // {"ORA-01013: пользователем запрошена отмена текущей операции"}
	protected final Integer ciFetchedRowLimit = 10000000; // Максимальное кол-во записей, которое может вернуть запрос к БД
	private Params params = null;
	private Long timeout = 60000L;
	private Boolean _isActive;
	private ResultSet resultSet = null;
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private SQLException lastError = null;

	protected String sql = null;
	protected String preparedSQL = null;
	protected HashMap<String, ?> rowValues = null;
	protected Long currentFetchedRowPosition = 0L;
	protected Boolean closeConnectionOnClose = false;

	public SQLCmdImpl() {
	}

	@Override
	public Boolean init(Connection conn, String sql, Params params, Long timeout) {
		Boolean result = true;
		this.connection = conn;
		this.lastError = null;
		this.timeout = timeout;
		this.sql = sql;
		this.params = params;
		try {
			this.prepareStatment();
		} catch (SQLException e) {
			this.lastError = e;
			result = false;
		}
		return result;
	}

	protected void prepareSQL() {
		this.preparedSQL = this.sql;
	}

	private void prepareStatment() throws SQLException {
		this.prepareSQL();
		this.preparedStatement = this.getConnection().prepareStatement(this.preparedSQL);
	}
	
	private void _doBeforeExecStatment(Params params){
		if (this.isActive())
			this.close();
		this.doBeforeOpen();
		this.setParamsToStatment(params);
	}
	
	@Override
	public Boolean open() {
		try {
			this._doBeforeExecStatment(this.params);
			this.resultSet = this.getStatement().executeQuery(this.getPreparedSQL());
			this._isActive = true;
			this.currentFetchedRowPosition = 0L;
			this.doAfterOpen();
			return true;
		} catch (SQLException e) {
			this.lastError = e;
			return false;
		}

	}

	public Boolean exec(Params params){
		try {
			this._doBeforeExecStatment(this.params);
			this.getStatement().execute(this.getPreparedSQL());
			this.doAfterOpen();
			return true;
		} catch (SQLException e) {
			this.lastError = e;
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T execScalar(Class<T> clazz, Params params){
		try {
			this._doBeforeExecStatment(this.params);
			ResultSet result = this.getStatement().executeQuery(this.getPreparedSQL());
			this.doAfterOpen();
			if(result.next())
				return (T)result.getObject(1);
			else
				return (T)null;
		} catch (SQLException e) {
			this.lastError = e;
			return (T)null;
		}
	}
	
	private void setParamsToStatment(Params params) {
		
	}

	
	
	private void doBeforeOpen() {
		// TODO Auto-generated method stub
	}

	private void doAfterOpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		this._isActive = false;
	}

	@Override
	public Boolean next() {
		// TODO Auto-generated method stub
		return null;
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
}
