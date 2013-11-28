package bio4j.database.api;

import bio4j.common.types.Params;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 27.11.13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
public class SQLCommandBeforeEventAttrs {
    private Params params;
    private Boolean cancel;

    public SQLCommandBeforeEventAttrs(Boolean cancel, Params params) {
        this.cancel = cancel;
        this.params = params;
    }

    public Params getParams() {
        return params;
    }

    public Boolean getCancel() {
        return cancel;
    }
}
