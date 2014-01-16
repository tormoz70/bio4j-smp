package ru.bio4j.smp.database.api;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 28.11.13
 * Time: 1:38
 * To change this template use File | Settings | File Templates.
 */
public class SQLConnectionPoolConfigBuilder {
    private String dbConnectionUrl;
    private String dbConnectionUsr;
    private String dbConnectionPwd;
    private int minPoolSize = 2;
    private int maxPoolSize = 10;
    private int connectionWaitTimeout = 5;
    private int initialPoolSize = 5;
    private String currentSchema = null;

    public SQLConnectionPoolConfigBuilder dbConnectionUrl(String value) {
        this.dbConnectionUrl = value;
        return this;
    }

    public SQLConnectionPoolConfigBuilder dbConnectionUsr (String value) {
        this.dbConnectionUsr = value;
        return this;
    }
    public SQLConnectionPoolConfigBuilder dbConnectionPwd (String value) {
        this.dbConnectionPwd = value;
        return this;
    }
    public SQLConnectionPoolConfigBuilder minPoolSize (int value) {
        this.minPoolSize = value;
        return this;
    }
    public SQLConnectionPoolConfigBuilder maxPoolSize (int value) {
        this.maxPoolSize = value;
        return this;
    }
    public SQLConnectionPoolConfigBuilder connectionWaitTimeout (int value) {
        this.connectionWaitTimeout = value;
        return this;
    }
    public SQLConnectionPoolConfigBuilder initialPoolSize (int value) {
        this.initialPoolSize = value;
        return this;
    }

    public SQLConnectionPoolConfigBuilder currentSchema (String value) {
        this.currentSchema = value;
        return this;
    }

    public SQLConnectionPoolConfig build() {
        return new SQLConnectionPoolConfig(this);
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


    public String getCurrentSchema() {
        return currentSchema;
    }
}
