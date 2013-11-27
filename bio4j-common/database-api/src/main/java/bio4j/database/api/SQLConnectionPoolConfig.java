package bio4j.database.api;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 28.11.13
 * Time: 1:38
 * To change this template use File | Settings | File Templates.
 */
public class SQLConnectionPoolConfig {
    private String dbConnectionUrl;
    private String dbConnectionUsr;
    private String dbConnectionPwd;
    private int minPoolSize = 2;
    private int maxPoolSize = 10;
    private int connectionWaitTimeout = 5;
    private int initialPoolSize = 5;

    public SQLConnectionPoolConfig(SQLConnectionPoolConfigBuilder builder) {
        this.dbConnectionUrl = builder.getDbConnectionUrl();
        this.dbConnectionUsr = builder.getDbConnectionUsr();
        this.dbConnectionPwd = builder.getDbConnectionPwd();
        this.minPoolSize = builder.getMinPoolSize();
        this.maxPoolSize = builder.getMaxPoolSize();
        this.connectionWaitTimeout = builder.getConnectionWaitTimeout();
        this.initialPoolSize = builder.getInitialPoolSize();
    }

    public String getDbConnectionUrl() {
        return dbConnectionUrl;
    }

    public String getDbConnectionUsr() {
        return dbConnectionUsr;
    }

    public String getDbConnectionPwd() {
        return dbConnectionPwd;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getConnectionWaitTimeout() {
        return connectionWaitTimeout;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

}
