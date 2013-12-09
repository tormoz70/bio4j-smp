package bio4j.database.direct.oracle.access.impl;

import ru.bio4j.smp.common.types.Params;
import oracle.jdbc.OraclePreparedStatement;

import java.sql.SQLException;

/**
 * Вытаскивает OUT параметры из statement и засовывает их в params
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraParamGetter {
    private OraCommandImpl owner;
    public OraParamGetter(OraCommandImpl owner) {
        this.owner = owner;
    }

    public void getParamsFromStatement(OraclePreparedStatement statement, Params params) throws SQLException {

    }
}
