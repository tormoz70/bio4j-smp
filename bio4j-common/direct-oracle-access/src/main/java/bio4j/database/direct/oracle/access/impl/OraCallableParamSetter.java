package bio4j.database.direct.oracle.access.impl;

import ru.bio4j.smp.common.types.Direction;
import ru.bio4j.smp.common.types.Param;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.RegexUtl;
import oracle.jdbc.OraclePreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Засовывает значения из params в CallableStatement
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraCallableParamSetter implements OraParamSetter {
    private static final Logger LOG = LoggerFactory.getLogger(OraCallableParamSetter.class);

    private OraCommand owner;
    public OraCallableParamSetter(OraCommand owner) {
        this.owner = owner;
    }

    @Override
    public void setParamsToStatement(PreparedStatement statement, Params params) throws SQLException {
        CallableStatement callable = (statement instanceof CallableStatement) ? (CallableStatement)statement : null;
        if(callable == null)
            throw new SQLException("Parameter [statement] mast be instance of CallableStatement!");
        final String sql = this.owner.getPreparedSQL();
        final List<String> paramsNames = OraUtils.extractParamNamesFromSQL(sql);
        final List<Param> outParams = new ArrayList<>();
        for (int i = 0; i < paramsNames.size(); i++) {
            String paramName = paramsNames.get(i);
            Param param = params.getParam(paramName);
            if (param != null) {
                param.setId(i + 1);
                if ((param.getDirection() == Direction.Input) || (param.getDirection() == Direction.InputOutput)) {
                    callable.setObject(paramName, param.getValue());
                }
                if ((param.getDirection() == Direction.Output) || (param.getDirection() == Direction.InputOutput)) {
                    outParams.add(param);
                }
            } else
                callable.setNull(paramName, Types.NULL);
        }
        for (Param outParam : outParams) {
            int sqlType = outParam.getSqlType();
            String paramName = outParam.getName();
            callable.registerOutParameter(paramName, sqlType);
        }
    }
}
