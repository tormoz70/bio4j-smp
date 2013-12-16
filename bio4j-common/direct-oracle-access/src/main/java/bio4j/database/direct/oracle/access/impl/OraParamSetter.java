package bio4j.database.direct.oracle.access.impl;

import oracle.jdbc.OracleCallableStatement;
import ru.bio4j.smp.common.types.Direction;
import ru.bio4j.smp.common.types.Param;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.RegexUtl;
import oracle.jdbc.OraclePreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Засовывает значения из params в statement
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraParamSetter {
    private static final Logger LOG = LoggerFactory.getLogger(OraParamSetter.class);

    private OraCommand owner;
    public OraParamSetter(OraCommand owner) {
        this.owner = owner;
    }

    public static List<String> extractParamNamesFromSQL(String sql) {
        LOG.debug("extractParamNamesFromSQL - start");
        LOG.debug("sql: " + sql);
        List<String> rslt = new ArrayList();
        LOG.debug("Удаляем все строковые константы");
        sql = RegexUtl.replace(sql, "(['])(.*?)\\1", "", true);
        LOG.debug("sql: " + sql);
        LOG.debug("Удаляем все многострочные коментарии");
        sql = RegexUtl.replace(sql, "[/]\\*.*?\\*[/]", "", true);
        LOG.debug("sql: " + sql);

        LOG.debug("Удаляем все операторы присвоения");
        sql = RegexUtl.replace(sql, ":=", "", true);
        LOG.debug("sql: " + sql);

        LOG.debug("Находим все параметры вида :qwe_ad");
        Matcher m = RegexUtl.match(sql, "(?<=:)\\b[\\w\\#\\$]+", true);
        while(m.find()) {
            String parName = m.group();
            LOG.debug(" - parName["+m.start()+"]: " + parName);
            if(rslt.indexOf(parName) == -1)
                rslt.add(parName);
        }
        LOG.debug("Найдено: " + rslt.size() + " параметров");
        return rslt;
    }

    /**
     * Добавляет отсутствующие парамеры в params
     * @param statement
     * @param params
     */
    private static void addMissedParamsFromStatement(String sql, OraclePreparedStatement statement, Params params) throws SQLException {
//        final OracleParameterMetaData paramMetadata = (OracleParameterMetaData)statement.getParameterMetaData();
//        final List<String> paramsNames = extractParamNamesFromSQL(sql);
//        for (int i = 0; i < paramsNames.size(); i++) {
//            if(!params.paramExists(paramsNames.get(i))) {
//                String paramType = ""+paramMetadata.getParameterTypeName(i);
//
//                params.add(paramsNames.get(i), paramType);
//        }   }
    }

    public void setParamsToStatement(OraclePreparedStatement statement, Params params) throws SQLException {
        //addMissedParamsFromStatement(sql, statement, params);
        //final OracleParameterMetaData paramMetadata = (OracleParameterMetaData)statement.getParameterMetaData();
        String sql = "";
        final List<String> paramsNames = extractParamNamesFromSQL(sql);
        final List<Param> outParams = new ArrayList<>();
        for (int i = 0; i < paramsNames.size(); i++) {
            String paramName = paramsNames.get(i);
            Param param = params.getParam(paramName);
            if (statement instanceof OraclePreparedStatement) {
                if (param != null) {
                    param.setId(i + 1);
                    if ((param.getDirection() == Direction.Input) || (param.getDirection() == Direction.InputOutput)) {
                        statement.setObjectAtName(paramName, param.getValue());
                    }
                    if ((param.getDirection() == Direction.Output) || (param.getDirection() == Direction.InputOutput)) {
                        outParams.add(param);
                    }
                } else
                    ((OraclePreparedStatement)statement).setNullAtName(paramName, Types.NULL);
            }

        }
        for (Param outParam : outParams) {
            if (statement instanceof OracleCallableStatement) {
                int sqlType = outParam.getSqlType();
                //if(Utl.arrayContains(new Integer[] {Types.DECIMAL, Types.NUMERIC}, sqlType))
                String paramName = outParam.getName();
                    ((OracleCallableStatement)statement).registerOutParameter(paramName, sqlType, 2);
                //else
                //    ((OracleCallableStatement)statement).registerOutParameter(outParam.getName(), sqlType);
            }
        }
    }
}
