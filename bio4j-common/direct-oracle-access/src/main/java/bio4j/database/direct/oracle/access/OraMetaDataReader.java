package bio4j.database.direct.oracle.access;

import bio4j.common.types.Params;
import bio4j.database.api.Field;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleResultSetMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Читает метаданные из ResultSet в Map<String,FileldMetaData>
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraMetaDataReader {
    private static final Logger LOG = LoggerFactory.getLogger(OraMetaDataReader.class);

    private OraCommandImpl owner;
    public OraMetaDataReader(OraCommandImpl owner) {
        this.owner = owner;
    }

    public Map<String,Field> read(ResultSet resultSet) throws SQLException {
        OracleResultSetMetaData metadata = (OracleResultSetMetaData)resultSet.getMetaData();
        Map<String, Field> newRow = new HashMap<>();
        for (int i = 1; i < metadata.getColumnCount(); i++) {
            LOG.debug(metadata.getColumnClassName(i));
            Class<?> type = null;
            try {
                type = getClass().getClassLoader().loadClass(metadata.getColumnClassName(i));
            } catch (ClassNotFoundException ex) {}
            String fieldName =  metadata.getColumnName(i);
            int fieldType = metadata.getColumnType(i);
            Field field = new FieldImpl(type, i, fieldName, fieldType);
            newRow.put(fieldName, field);
        }
        return newRow;
    }
}
