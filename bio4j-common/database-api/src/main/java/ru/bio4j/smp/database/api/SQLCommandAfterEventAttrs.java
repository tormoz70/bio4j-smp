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
public class SQLCommandAfterEventAttrs {
    private Params params;
    private ResultSet resultSet;
    private Object resultScalarValue;
    private Exception exception;

    public static SQLCommandAfterEventAttrs build (Params params, ResultSet resultSet, Exception ex) {
        SQLCommandAfterEventAttrs rslt = new SQLCommandAfterEventAttrs();
        rslt.params = params;
        rslt.resultSet = resultSet;
        rslt.exception = ex;
        return rslt;
    }

    public static SQLCommandAfterEventAttrs build (Params params, Object resultValue, Exception ex) {
        SQLCommandAfterEventAttrs rslt = new SQLCommandAfterEventAttrs();
        rslt.params = params;
        rslt.resultScalarValue = resultValue;
        rslt.exception = ex;
        return rslt;
    }

    public static SQLCommandAfterEventAttrs build (Params params, Exception ex) {
        SQLCommandAfterEventAttrs rslt = new SQLCommandAfterEventAttrs();
        rslt.params = params;
        rslt.exception = ex;
        return rslt;
    }

    public Params getParams() {
        return params;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public Object getResultScalarValue() {
        return resultScalarValue;
    }

    public Exception getException() {
        return exception;
    }
}
