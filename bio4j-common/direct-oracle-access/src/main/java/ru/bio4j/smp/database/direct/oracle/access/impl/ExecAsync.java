package ru.bio4j.smp.database.direct.oracle.access.impl;


import ru.bio4j.smp.database.api.SQLConnectionPool;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 17.12.13
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
public class ExecAsync <T> {
    private SQLConnectionPool connectionPool;
    private T processResult;
    public ExecAsync(SQLConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void exec() {

    }

}
