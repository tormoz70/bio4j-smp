package ru.bio4j.smp.database.direct.oracle.access.impl;

import oracle.jdbc.OracleCallableStatement;
import ru.bio4j.smp.common.types.Direction;
import ru.bio4j.smp.common.types.Param;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.Utl;

import java.sql.SQLException;

/**
 * Вытаскивает OUT параметры из statement и засовывает их в params
 */
public class OraCallableParamGetter implements OraParamGetter {
    private OraCommand owner;
    public OraCallableParamGetter(OraCommand owner) {
        this.owner = owner;
    }

    public void getParamsFromStatement(OracleCallableStatement statement, Params params) throws SQLException {
        for(Param param : this.owner.getParams()) {
            if (Utl.arrayContains(new Direction[] {Direction.InputOutput, Direction.Output}, param.getDirection())) {
                String paramName = param.getName();
                Object outValue = statement.getObject(paramName);
                param.setValue(outValue);
            }

        }
    }
}
