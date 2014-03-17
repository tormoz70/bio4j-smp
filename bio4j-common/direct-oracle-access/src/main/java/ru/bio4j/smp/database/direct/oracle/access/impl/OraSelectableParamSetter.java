package ru.bio4j.smp.database.direct.oracle.access.impl;

import oracle.jdbc.OraclePreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bio4j.smp.common.types.Param;
import ru.bio4j.smp.common.types.Params;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Засовывает значения из params в OraclePreparedStatement
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraSelectableParamSetter implements OraParamSetter {
    private static final Logger LOG = LoggerFactory.getLogger(OraSelectableParamSetter.class);

    private OraCommand owner;
    public OraSelectableParamSetter(OraCommand owner) {
        this.owner = owner;
    }

    @Override
    public void setParamsToStatement(PreparedStatement statement, Params params) throws SQLException {
        OraclePreparedStatement selectable = (statement instanceof OraclePreparedStatement) ? (OraclePreparedStatement)statement : null;
        if(selectable == null)
            throw new SQLException("Parameter [statement] mast be instance of OraclePreparedStatement!");
        final String sql = this.owner.getPreparedSQL();
        final List<String> paramsNames = OraUtils.extractParamNamesFromSQL(sql);
        for (int i = 0; i < paramsNames.size(); i++) {
            String paramName = paramsNames.get(i);
            Param param = params.getParam(paramName);
            if (param != null) {
                param.setId(i + 1);
                selectable.setObjectAtName(paramName, param.getValue());
            } else
                selectable.setNullAtName(paramName, Types.NULL);
        }
    }
}
