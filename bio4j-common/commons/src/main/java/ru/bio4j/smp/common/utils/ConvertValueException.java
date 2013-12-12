package ru.bio4j.smp.common.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConvertValueException extends Exception {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("commons",
            Locale.ROOT, ConvertValueException.class.getClassLoader());

	public ConvertValueException(Object value, Class<?> valueType, Class<?> targetType) {
        super(String.format(resourceBundle.getString("error.message.convert.value"), value, valueType, targetType.getName()));
    }

}
