package bio4j.database.direct.oracle.access;

import bio4j.common.types.Param;
import bio4j.common.types.Params;
import bio4j.common.utils.RegexUtl;
import bio4j.common.utils.StringUtl;
import bio4j.database.api.SQLConnectionPool;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
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

    private OraCommandImpl owner;
    public OraParamSetter(OraCommandImpl owner) {
        this.owner = owner;
    }

    public static List<String> extractParamNamesFromSQL(String sql) {
        List<String> rslt = new ArrayList();
        // Удаляем все строковый константы
        sql = RegexUtl.replace(sql, "(['])(.*?)\\1", "", true);

        // Находим все параметры вида :qwe_ad
        Matcher m = RegexUtl.match(sql, "(?<=:)\\b[\\w\\#\\$]+", true);
        while(m.find()) {
            String parName = m.group();
            if(rslt.indexOf(parName) == -1)
                rslt.add(parName);
        }
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

    public void setParamsToStatement(String sql, OraclePreparedStatement statement, Params params) throws SQLException {
        //addMissedParamsFromStatement(sql, statement, params);
        //final OracleParameterMetaData paramMetadata = (OracleParameterMetaData)statement.getParameterMetaData();

        final List<String> paramsNames = extractParamNamesFromSQL(sql);
        for (int i = 0; i < paramsNames.size(); i++) {
            Param param = params.getParam(paramsNames.get(i));
            if(param != null) {
                statement.setObjectAtName(paramsNames.get(i), param.getValue());
            } else
                statement.setNullAtName(paramsNames.get(i), Types.NULL);
        }
    }
}
