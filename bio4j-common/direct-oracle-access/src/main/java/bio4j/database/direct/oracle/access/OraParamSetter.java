package bio4j.database.direct.oracle.access;

import bio4j.common.types.Params;
import bio4j.common.utils.RegexUtl;
import bio4j.common.utils.StringUtl;
import bio4j.database.api.SQLConnectionPool;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static List<String> extractParamNamesFromSQL(String sql) {
        List<String> rslt = new ArrayList();
        // Удаляем все строковый константы
        sql = RegexUtl.replace(sql, "(['])(.*?)\\1", "", true);

        // Находим все параметры вида :qwe_ad
        Matcher m = RegexUtl.match(sql, "(?<=:)\\b[\\w\\#\\$]+", true);
        while(m.find()) {
            String parName = m.group();
            if(rslt.indexOf(parName) == 0)
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
        final OracleParameterMetaData paramMetadata = statement.OracleGetParameterMetaData();
        final List<String> paramsNames = extractParamNamesFromSQL(sql);
        for (int i = 0; i < paramsNames.size(); i++) {
            String paramName = paramsNames.get(i);
            ///...
        }
    }

    public void setParamsToStatment(String sql, OraclePreparedStatement statement, Params params) throws SQLException {
        addMissedParamsFromStatement(sql, statement, params);

    }
}
