package ru.bio4j.smp.common.utils;

public class ConvertValueException extends Exception {

	public ConvertValueException(Object value, Class<?> valueType, Class<?> targetType) {
	    super("Значение [" + value + "] типа [" + valueType + "] не может быть представлено как [" + targetType.getName() + "]!!! ");
    }

}
