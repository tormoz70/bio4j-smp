package bio4j.common.utils;

public class ConverterValueException extends Exception {

	/**
	 * 
	 */
    private static final long serialVersionUID = 5073556618292324366L;

	public ConverterValueException(Object value, Class<?> valueType, Class<?> targetType) {
	    super("Значение [" + value + "] типа [" + valueType + "] не может быть представлено как [" + targetType.getName() + "]!!! ");
    }

}
