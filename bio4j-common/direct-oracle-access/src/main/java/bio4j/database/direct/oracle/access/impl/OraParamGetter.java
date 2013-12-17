package bio4j.database.direct.oracle.access.impl;

import oracle.jdbc.OracleCallableStatement;
import ru.bio4j.smp.common.types.Direction;
import ru.bio4j.smp.common.types.Param;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.Utl;

import java.sql.SQLException;

/**
 * Вытаскивает OUT параметры из statement и засовывает их в params
 */
public interface OraParamGetter {
    public void getParamsFromStatement(OracleCallableStatement statement, Params params) throws SQLException;
}
