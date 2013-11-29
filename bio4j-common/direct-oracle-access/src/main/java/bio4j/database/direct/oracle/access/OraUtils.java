package bio4j.database.direct.oracle.access;

import bio4j.common.utils.RegexUtl;
import bio4j.common.utils.StringUtl;
import bio4j.database.api.SQLConnectionPool;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

/**
 * Утилиты для работы с метаданными СУБД Oracle
 */
public class OraUtils {
	public static class PackageName {
		public final String pkgName;
		public final String methodName;
		public PackageName(String pkgName, String methodName) {
			this.pkgName = pkgName;
			this.methodName = methodName;
		}
	}

    /**
     * Вытаскивает из SQL имя пакета и метода
     * @param sql
     * @return
     */
    public static OraUtils.PackageName detectAutoDBMSParamsParts(String sql) {
        String pkgName = RegexUtl.find(sql, "\\b[\\w$]+\\b(?=[.])", true);
        String methodName = RegexUtl.find(sql, "(?<=[.])\\b[\\w$]+\\b(?=\\s*[(;])", true);
        PackageName pkg = new OraUtils.PackageName(pkgName, methodName);
    	return pkg;
    }


    /**
     * Вытаскивает из SQL все вызовы хранимых процедур
     * @param sql
     * @return
     */
    public static String[] detectExecsOfStoredProcs(String sql) {
        final String csDelimiter = "+|+";
        String resultStr = null;
        Matcher m = RegexUtl.match(sql, "\\b[\\w$]+\\b[.]\\b[\\w$]+\\b\\s*[(]\\s*[$]PRMLIST\\s*[)]", true);
        while(m.find())
            resultStr = StringUtl.append(resultStr, m.group(), csDelimiter);
        return !StringUtl.isNullOrEmpty(resultStr) ? StringUtl.split(resultStr, csDelimiter) : new String[0];
    }


    private static final String SQL_GET_PARAMS_FROM_DBMS = "select "+
            " argument_name, position, sequence, data_type, in_out, data_length" +
            " from user_arguments" +
            " where package_name = upper(:package_name)" +
            " and object_name = upper(:method_name)" +
            " order by position";
    private static final String DEFAULT_PARAM_PREFIX = "P_";
    public static String detectSQLParamsAuto(String sql, Connection conn) throws SQLException {

        boolean isParamListAuto = RegexUtl.match(sql, "[(]\\s*[$]PRMLIST\\s*[)]", true).matches();

        if(isParamListAuto) {
            String[] execs = OraUtils.detectExecsOfStoredProcs(sql);
            for (String t : execs) {
                StringBuilder args = new StringBuilder();
                OraUtils.PackageName pkg = OraUtils.detectAutoDBMSParamsParts(t);
                try (OraclePreparedStatement st = (OraclePreparedStatement)conn.prepareStatement(SQL_GET_PARAMS_FROM_DBMS, ResultSet.TYPE_FORWARD_ONLY)) {
                    st.setStringAtName("package_name", pkg.pkgName);
                    st.setStringAtName("object_name", pkg.methodName);
                    try (OracleResultSet rs = (OracleResultSet)st.executeQuery()) {
                        while(rs.next()) {
                            String parName = rs.getString("argument_name");
                            if(!parName.startsWith(DEFAULT_PARAM_PREFIX))
                                throw new IllegalArgumentException("Не верный формат наименования аргументов хранимой процедуры.\n" +
                                        "Для использования автогенерации аргументов с помощью переменной $PRMLIST\n" +
                                        "необходимо, чтобы все имена аргументов начинались с префикса " + DEFAULT_PARAM_PREFIX + " !");
                            parName = parName.substring(2);
                            args.append(((args.length() == 0) ? ":" : ",:") + parName);
                        }
                    }
                }
                String newExec = t;
                newExec = RegexUtl.replace(newExec, "[(]\\s*[$]PRMLIST\\s*[)]", "(" + args + ")", true);
                sql = sql.replaceAll(t, newExec);
            }
        }
        return sql;
    }

}
