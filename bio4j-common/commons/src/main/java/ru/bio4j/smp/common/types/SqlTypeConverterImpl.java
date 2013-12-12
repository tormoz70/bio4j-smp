package ru.bio4j.smp.common.types;

import ru.bio4j.smp.common.utils.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;

/**
 * Default SqlTypeConverterImpl
 */
public class SqlTypeConverterImpl implements SqlTypeConverter {
    public Class<?> read (int sqlType, int charSize) {
        switch (sqlType) {
            case Types.CHAR :
            case Types.VARCHAR :
            case Types.NCHAR :
            case Types.NVARCHAR :
            case Types.CLOB :
            case Types.NCLOB :
            case Types.LONGNVARCHAR :
            case Types.LONGVARCHAR :
                return ((charSize == 1) ? Character.class : String.class);
            case Types.BIGINT :
            case Types.INTEGER :
                return BigInteger.class;
            case Types.DOUBLE :
            case Types.FLOAT :
            case Types.DECIMAL :
            case Types.NUMERIC :
            case Types.REAL :
            case Types.SMALLINT :
                return BigDecimal.class;
            case Types.BLOB :
            case Types.BINARY :
            case Types.LONGVARBINARY :
            case Types.VARBINARY :
                return Byte[].class;
            case Types.DATE :
            case Types.TIMESTAMP :
                return Date.class;
            default:
                return Object.class;
        }
    }
    public int write (Class<?> type, int stringSize, boolean isCallableStatment) {
        if ((type == String.class) ||
                (type == Character.class)) {
            return (stringSize <= (isCallableStatment ? 32000 : 4000)) ? Types.VARCHAR : Types.CLOB;
        } else if (Converter.typeIsNumber(type)) {
            return Types.NUMERIC;
        } else if ((type == boolean.class) || (type == Boolean.class)) {
            return Types.CHAR;
        } else if ((type == java.util.Date.class) || (type == java.sql.Date.class) || (type == java.sql.Timestamp.class)) {
            return Types.DATE;
        } else if ((type == byte[].class)||(type == Byte[].class)) {
            return Types.BLOB;
        } else
            return Types.NULL;
    }
}
