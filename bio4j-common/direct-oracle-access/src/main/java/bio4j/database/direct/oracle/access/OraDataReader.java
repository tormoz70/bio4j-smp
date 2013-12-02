package bio4j.database.direct.oracle.access;

import bio4j.common.types.Params;
import bio4j.database.api.Field;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSetMetaData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Читает данные из ResultSet в массив
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraDataReader {
    private OraCommandImpl owner;
    public OraDataReader(OraCommandImpl owner) {
        this.owner = owner;
    }

    public void read(ResultSet resultSet, Map<String, Field> toRow) throws SQLException {
        OracleResultSetMetaData metadata = (OracleResultSetMetaData)resultSet.getMetaData();
        for (int i = 1; i < metadata.getColumnCount(); i++) {
            toRow.get(metadata.getColumnName(i)).setValue(resultSet.getObject(i));
        }
    }
}
