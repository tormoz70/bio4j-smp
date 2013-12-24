package bio4j.database.direct.oracle.access.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.*;
import ru.bio4j.smp.common.types.DelegateSQLAction;
import ru.bio4j.smp.common.types.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.database.api.*;

/**
 * Базовый класс
 */
public abstract class OraCommand implements SQLCommand {
    private static final Logger LOG = LoggerFactory.getLogger(OraCommand.class);

	public static final int ORAERRCODE_APP_ERR_START = 20000; // начало диапазона кодов ошибок приложения в Oracle
    public static final int ORAERRCODE_USER_BREAKED = 1013; // {"ORA-01013: пользователем запрошена отмена текущей операции"}
	protected Params params = null;
    protected int timeout = 60;
    protected OracleConnection connection = null;
    protected OraclePreparedStatement preparedStatement = null;
    protected OracleResultSet resultSet = null;
    protected Exception lastError = null;
    protected String preparedSQL = null;

    protected SQLWrapper sqlWrapper;

    protected OraParamSetter paramSetter;
    protected OraParamGetter paramGetter;

	protected boolean closeConnectionOnFinish = false;

    protected List<SQLCommandBeforeEvent> beforeEvents = new ArrayList<>();
    protected List<SQLCommandAfterEvent> afterEvents = new ArrayList<>();
    protected List<SQLCommandExecErrorEvent> execErrorEvents = new ArrayList<>();

    @Override
    public void addBeforeEvent(SQLCommandBeforeEvent e) {
        this.beforeEvents.add(e);
    }

    @Override
    public void addAfterEvent(SQLCommandAfterEvent e) {
        this.afterEvents.add(e);
    }

    @Override
    public void clearBeforeEvents() {
        this.beforeEvents.clear();
    }

    @Override
    public void clearAfterEvents() {
        this.afterEvents.clear();
    }

    public OraCommand() {
	}

    /**
     * Присваивает значения входящим параметрам
     */
    protected void setParamsToStatement() throws SQLException {
        if(this.paramSetter != null)
            this.paramSetter.setParamsToStatement(this.preparedStatement, this.params);
    }

    protected void getBackOutParams() throws SQLException {
        if((this.paramGetter != null) && (this.preparedStatement instanceof OracleCallableStatement))
            this.paramGetter.getParamsFromStatement((OracleCallableStatement)this.preparedStatement, this.params);
    }

    public void setParamSetter(OraParamSetter paramSetter) {
        this.paramSetter = paramSetter;
    }

    public void setParamGetter(OraParamGetter paramGetter) {
        this.paramGetter = paramGetter;
    }

	public boolean init(Connection conn, Params params, int timeout) {
		this.connection = (OracleConnection)conn;
		this.lastError = null;
		this.timeout = timeout;
		this.params = params;
		return this.prepareStatement();
	}
    public boolean init(Connection conn, Params params) {
        return this.init(conn, params, 60);
    }

	protected abstract boolean prepareStatement();

    protected boolean doBeforeStatement(Params params){
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

    protected void doAfterStatement(SQLCommandAfterEventAttrs attrs){
        if(this.afterEvents.size() > 0) {
            for(SQLCommandAfterEvent e : this.afterEvents)
                e.handle(this, attrs);
        }
    }

    protected void doOnExecuteError(SQLCommandExecErrorEventAttrs attrs){
        if(this.execErrorEvents.size() > 0) {
            for(SQLCommandExecErrorEvent e : this.execErrorEvents)
                e.handle(this, attrs);
        }
    }

    protected <T> Object processStatement(Params params, DelegateSQLAction<T> action) {
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
                result = false;
                this.lastError = e;
                //LOG.error("Error on exec sql : [" + this.preparedSQL + "]", e);
                this.doOnExecuteError(SQLCommandExecErrorEventAttrs.build(this.params, e));
            }
            this.doAfterStatement(SQLCommandAfterEventAttrs.build( // Обрабатываем события
                    this.params, this.resultSet, this.lastError
            ));
        } finally {
            return result; // Возвращаем результат
        }
    }

    protected void resetCommand() {
        this.lastError = null;
    }



//	@SuppressWarnings("unchecked")
//    @Override
//	public <T> T execScalar(Class<T> clazz, Params params){
//        return (T)this.processStatement(params, new DelegateSQLAction<T>() {
//            @Override
//            public T execute() throws SQLException {
//                final OraCommand self = OraCommand.this;
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
	public Params getParams() {
		return this.params;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public Statement getStatement() {
		return this.preparedStatement;
	}

	@Override
	public Exception getLastError() {
		return this.lastError;
	}

    @Override
    public void setSqlWrapper(SQLWrapper sqlWrapper) {
        this.sqlWrapper = sqlWrapper;
    }


    @Override
    public String getPreparedSQL() {
        return this.preparedSQL;
    }

}
