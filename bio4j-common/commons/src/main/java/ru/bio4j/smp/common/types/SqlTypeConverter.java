package ru.bio4j.smp.common.types;

import ru.bio4j.smp.common.utils.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;

/**
 * Default SqlTypeConverterImpl
 */
public interface SqlTypeConverter {
    Class<?> read (int sqlType, int charSize);
    int write (Class<?> type, int stringSize, boolean isCallableStatment);
}
