package bio4j.database.direct.oracle.access.impl;

import java.sql.Connection;
import java.sql.SQLException;

import ru.bio4j.smp.common.utils.StringUtl;
import ru.bio4j.smp.common.utils.Utl;
import ru.bio4j.smp.database.api.SQLConnectionPool;
import ru.bio4j.smp.database.api.SQLConnectionPoolConfig;
import ru.bio4j.smp.database.api.SQLConnectionPoolStat;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OraConnectionPoolImpl implements SQLConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(OraCommandImpl.class);

    private PoolDataSource cpool;

    private OraConnectionPoolImpl(PoolDataSource cpool) {
        this.cpool = cpool;
    }

    public static OraConnectionPoolImpl create(String poolName, SQLConnectionPoolConfig config) throws SQLException {
        LOG.debug("Creating SQLConnectionPool with:\n" + Utl.buildBeanStateInfo(config, null, "\t"));

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
        return new OraConnectionPoolImpl(pool);
    }

    public void Close(){
        //
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

    @Override
    public SQLConnectionPoolStat getStat(){
        return new SQLConnectionPoolStat(
            this.cpool.getConnectionPoolName(), this.cpool.getMinPoolSize(), this.cpool.getMaxPoolSize(), this.cpool.getConnectionWaitTimeout(), this.cpool.getInitialPoolSize(),
            this.cpool.getStatistics().getTotalConnectionsCount(), this.cpool.getStatistics().getAvailableConnectionsCount(), this.cpool.getStatistics().getBorrowedConnectionsCount(),
            this.cpool.getStatistics().getAverageBorrowedConnectionsCount(), this.cpool.getStatistics().getPeakConnectionsCount(), this.cpool.getStatistics().getRemainingPoolCapacityCount(),
            this.cpool.getStatistics().getLabeledConnectionsCount(), this.cpool.getStatistics().getConnectionsCreatedCount(), this.cpool.getStatistics().getConnectionsClosedCount(),
            this.cpool.getStatistics().getAverageConnectionWaitTime(), this.cpool.getStatistics().getPeakConnectionWaitTime(), this.cpool.getStatistics().getAbandonedConnectionsCount(),
            this.cpool.getStatistics().getPendingRequestsCount(), this.cpool.getStatistics().getCumulativeConnectionWaitTime(), this.cpool.getStatistics().getCumulativeConnectionBorrowedCount(),
            this.cpool.getStatistics().getCumulativeConnectionUseTime(), this.cpool.getStatistics().getCumulativeConnectionReturnedCount(), this.cpool.getStatistics().getCumulativeSuccessfulConnectionWaitTime(),
            this.cpool.getStatistics().getCumulativeSuccessfulConnectionWaitCount(), this.cpool.getStatistics().getCumulativeFailedConnectionWaitTime(),
            this.cpool.getStatistics().getCumulativeFailedConnectionWaitCount()
        );
    }

}
