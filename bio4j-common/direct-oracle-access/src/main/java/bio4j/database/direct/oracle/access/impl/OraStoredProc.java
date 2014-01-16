package bio4j.database.direct.oracle.access.impl;

import oracle.jdbc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.common.types.DelegateSQLAction;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.database.api.SQLStoredProc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Реализует 3 основных вида запроса Query, Exec, Scalar
 */
public class OraStoredProc extends OraCommand implements SQLStoredProc {
    private static final Logger LOG = LoggerFactory.getLogger(OraStoredProc.class);

    private String storedProcName;

    public OraStoredProc() {
	}

	@Override
	public boolean init(Connection conn, String storedProcName, Params params, int timeout) {
        this.storedProcName = storedProcName;
		return super.init(conn, params, timeout);
	}
    @Override
    public boolean init(Connection conn, String storedProcName, Params params) {
        return this.init(conn, storedProcName, params, 60);
    }

    @Override
	protected boolean prepareStatement() {
        try {
            this.preparedSQL = OraUtils.detectStoredProcParamsAuto(this.storedProcName, this.connection);
            this.preparedSQL = "{call " + this.preparedSQL + "}";
            this.preparedStatement = (OracleCallableStatement)this.connection.prepareCall(this.preparedSQL);
            this.preparedStatement.setQueryTimeout(this.timeout);
            return true;
        } catch (SQLException ex) {
            this.lastError = ex;
            LOG.error("Error!!!", ex);
            return false;
        }
	}
	
    @Override
	public boolean execSQL(Params params) {
        return (boolean)this.processStatement(params, new DelegateSQLAction<Boolean>() {
            @Override
            public Boolean execute() throws SQLException {
                final OraStoredProc self = OraStoredProc.this;
                self.preparedStatement.execute();
                return true;
            }
        });
	}

    @Override
    public boolean execSQL() {
        return this.execSQL(null);
    }


}
