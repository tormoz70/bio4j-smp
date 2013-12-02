package bio4j.database.direct.oracle.access;

import bio4j.common.types.Params;
import bio4j.common.utils.RegexUtl;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;

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
