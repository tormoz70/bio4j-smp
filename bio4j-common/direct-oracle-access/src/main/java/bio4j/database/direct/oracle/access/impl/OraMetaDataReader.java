package bio4j.database.direct.oracle.access.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Читает метаданные из ResultSet в Map<String,FileldMetaData>
 * User: ayrat
 * Date: 29.11.13
 * Time: 17:13
 */
public class OraMetaDataReader {
    private static final Logger LOG = LoggerFactory.getLogger(OraMetaDataReader.class);

    private OraCommand owner;
    public OraMetaDataReader(OraCommand owner) {
        this.owner = owner;
    }

}
