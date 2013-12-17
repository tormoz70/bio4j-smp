package bio4j.database.direct.oracle.access.impl;

import ru.bio4j.smp.common.types.Params;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 17.12.13
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
public interface OraParamSetter {
    void setParamsToStatement(PreparedStatement statement, Params params) throws SQLException;
}
