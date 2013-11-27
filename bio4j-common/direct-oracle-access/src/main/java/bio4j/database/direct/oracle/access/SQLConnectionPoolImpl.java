package bio4j.database.direct.oracle.access;

import java.sql.Connection;
import java.sql.SQLException;

import bio4j.common.utils.StringUtl;
import bio4j.database.api.SQLConnectionPool;
import bio4j.database.api.SQLConnectionPoolConfig;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLConnectionPoolImpl implements SQLConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(SQLCommandImpl.class);

    private PoolDataSource cpool;

    private SQLConnectionPoolImpl(PoolDataSource cpool) {
        this.cpool = cpool;
    }

    public static SQLConnectionPoolImpl create(String poolName, SQLConnectionPoolConfig config) {
        try {
            PoolDataSource pool = new PoolDataSourceImpl();
            pool.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            pool.setURL(config.getDbConnectionUrl());

            pool.setUser(config.getDbConnectionUsr());
            pool.setPassword(config.getDbConnectionPwd());
            pool.setConnectionPoolName(poolName);
            pool.setMinPoolSize(config.getMinPoolSize());
            pool.setMaxPoolSize(config.getMaxPoolSize());
            pool.setConnectionWaitTimeout(config.getConnectionWaitTimeout());
            pool.setInitialPoolSize(config.getInitialPoolSize());
            return new SQLConnectionPoolImpl(pool);
        } catch (SQLException ex) {
            LOG.error("Error!!!", ex);
            return null;
        }
    }

	@Override
	public Connection getConnection(String userName, String password) {
        try {
            if(StringUtl.isNullOrEmpty(userName))
                return this.cpool.getConnection();
            else
                return this.cpool.getConnection(userName, password);
        } catch (SQLException ex) {
            LOG.error("Error!!!", ex);
            return null;
        }
	}
    @Override
    public Connection getConnection() {
        return getConnection(null, null);
    }

}
