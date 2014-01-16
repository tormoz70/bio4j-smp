package bio4j.database.direct.oracle.access.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.bio4j.smp.common.utils.StringUtl;
import ru.bio4j.smp.common.utils.Utl;
import ru.bio4j.smp.database.api.*;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OraConnectionPool implements SQLConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(OraCommand.class);

    private PoolDataSource cpool;

    private final List<SQLConnectionAfterEvent> afterEvents = new ArrayList<>();
    private final List<SQLConnectionAfterEvent> innerAfterEvents = new ArrayList<>();

    private final SQLConnectionPoolConfig config;

    private OraConnectionPool(PoolDataSource cpool, SQLConnectionPoolConfig config) {
        this.cpool = cpool;
        this.config = config;
        if(this.config.getCurrentSchema() != null) {
            this.innerAfterEvents.add(
                    new SQLConnectionAfterEvent() {
                        @Override
                        public void handle(SQLConnectionPool sender, SQLConnectionAfterEventAttrs attrs) throws SQLException {
                            if(attrs.getConnection() != null) {
                                String curSchema = OraConnectionPool.this.config.getCurrentSchema().toUpperCase();
                                LOG.debug("onAfterGetConnection - start setting current_schema="+curSchema);
                                CallableStatement cs1 = attrs.getConnection().prepareCall( "alter session set current_schema="+curSchema);
                                cs1.execute();
                                LOG.debug("onAfterGetConnection - OK. current_schema now is "+curSchema);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void addAfterEvent(SQLConnectionAfterEvent e) {
        this.afterEvents.add(e);
    }

    @Override
    public void clearAfterEvents() {
        this.afterEvents.clear();
    }

    private void doAfterConnect(SQLConnectionAfterEventAttrs attrs) throws SQLException {
        if(this.innerAfterEvents.size() > 0) {
            for(SQLConnectionAfterEvent e : this.innerAfterEvents)
                e.handle(this, attrs);
        }
        if(this.afterEvents.size() > 0) {
            for(SQLConnectionAfterEvent e : this.afterEvents)
                e.handle(this, attrs);
        }
    }

    public static OraConnectionPool create(String poolName, SQLConnectionPoolConfig config) throws SQLException {
        LOG.debug("Creating SQLConnectionPool with:\n" + Utl.buildBeanStateInfo(config, null, "\t"));

        PoolDataSource pool = new PoolDataSourceImpl();
        pool.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pool.setURL(config.getDbConnectionUrl());
        pool.setConnectionProperty("autoCommit", "false");

        pool.setUser(config.getDbConnectionUsr());
        pool.setPassword(config.getDbConnectionPwd());
        pool.setConnectionPoolName(poolName);
        pool.setMinPoolSize(config.getMinPoolSize());
        pool.setMaxPoolSize(config.getMaxPoolSize());
        pool.setConnectionWaitTimeout(config.getConnectionWaitTimeout());
        pool.setInitialPoolSize(config.getInitialPoolSize());

        return new OraConnectionPool(pool, config);
    }

    public void Close(){
        //
    }



	@Override
	public Connection getConnection(String userName, String password) throws SQLException {
        Connection conn = null;

        if(StringUtl.isNullOrEmpty(userName))
            conn = this.cpool.getConnection();
        else
            conn = this.cpool.getConnection(userName, password);

        this.doAfterConnect(SQLConnectionAfterEventAttrs.build(conn));
        return conn;
	}

    @Override
    public Connection getConnection() throws SQLException {
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
