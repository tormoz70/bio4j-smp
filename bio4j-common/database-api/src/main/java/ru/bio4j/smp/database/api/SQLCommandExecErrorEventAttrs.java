package ru.bio4j.smp.database.api;

import ru.bio4j.smp.common.types.Params;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 27.11.13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
public class SQLCommandExecErrorEventAttrs {
    private Params params;
    private Exception exception;

    public static SQLCommandExecErrorEventAttrs build (Params params, Exception ex) {
        SQLCommandExecErrorEventAttrs rslt = new SQLCommandExecErrorEventAttrs();
        rslt.params = params;
        rslt.exception = ex;
        return rslt;
    }

    public Params getParams() {
        return params;
    }

    public Exception getException() {
        return exception;
    }
}
