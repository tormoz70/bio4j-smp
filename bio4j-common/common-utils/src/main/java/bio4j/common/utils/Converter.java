package bio4j.common.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.print.DocFlavor.CHAR_ARRAY;

/**
 * @author ayrat
 *	Конвертер типов
 */
public class Converter {

	public static Boolean typeIsNumber(Class<?> inType) {
		if ((inType == Byte.class) || (inType == Short.class) || (inType == Integer.class) || (inType == Long.class) || (inType == Float.class) || (inType == Double.class))
			return true;
		return false;
	}

	private static Object prepareInValue(Object inValue) {
		Class<?> inType = (inValue == null) ? null : inValue.getClass();
		if (inType == boolean.class)
			return new Boolean((boolean) inValue);
		if (inType == byte.class)
			return new Byte((byte) inValue);
		if (inType == short.class)
			return new Short((short) inValue);
		if (inType == int.class)
			return new Integer((int) inValue);
		if (inType == long.class)
			return new Long((long) inValue);
		if (inType == char.class)
			return new Character((char) inValue);
		if (inType == float.class)
			return new Float((float) inValue);
		if (inType == double.class)
			return new Double((double) inValue);
		return inValue;
	}

	private static Class<?> prepareInType(Class<?> inType) {
		if (inType == boolean.class)
			return Boolean.class;
		if (inType == byte.class)
			return Byte.class;
		if (inType == short.class)
			return Short.class;
		if (inType == int.class)
			return Integer.class;
		if (inType == long.class)
			return Long.class;
		if (inType == char.class)
			return Character.class;
		if (inType == float.class)
			return Float.class;
		if (inType == double.class)
			return Double.class;
		return inType;
	}

	public static Double parsDouble(String inValue) {
		DecimalFormat fmt = new DecimalFormat("##################.##########");
		DecimalFormatSymbols dfs = fmt.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		fmt.setDecimalFormatSymbols(dfs);
		try {
			Object vval = fmt.parse(inValue);
			if(vval.getClass() == Double.class)
				return (Double)vval;
			if(vval.getClass() == Long.class)
				return ((Long)vval).doubleValue();
			return null;
        } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
        }
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T string2Number(String inValue, Class<T> targetType) {
		inValue = StringUtl.isNullOrEmpty(inValue) ? "0" : inValue.trim().replace(" ", "").replace(',', '.');
		return number2Number(parsDouble(inValue), targetType);
	}

	@SuppressWarnings("unchecked")
    public static <T> T number2Number(Number inValue, Class<T> targetType) {
		if (targetType == Byte.class)
			return (T)new Byte(inValue.byteValue());
		if (targetType == Short.class)
			return (T)new Short(inValue.shortValue());
		if (targetType == Integer.class)
			return (T)new Integer(inValue.intValue());
		if (targetType == Long.class)
			return (T)new Long(inValue.longValue());
		if (targetType == Float.class) 
			return (T)new Float(inValue.floatValue());
		if (targetType == Double.class)
			return (T)new Double(inValue.doubleValue());
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toType(Object inValue, Class<T> targetType) throws ConverterValueException {
		if ((targetType != null) && (inValue != null) && (inValue.getClass() == targetType))
			return (T) inValue;
		inValue = prepareInValue(inValue);
		if (targetType.isPrimitive()) {
			return (T) toType(inValue, prepareInType(targetType));
		}
		Class<?> inType = (inValue == null) ? null : inValue.getClass();
		if (targetType != null) {
			if (inType != null) {
				if (targetType == java.util.Date.class) {
					if (inType == java.util.Date.class) {
						return (T) inValue;
					} else if (inType == String.class) {
						try {
							return (T) DateTimeParser.getInstance().pars((String) inValue);
						} catch (DateParseException ex) {
							throw new ConverterValueException(inValue, inType, targetType);
						}
					} else {
						throw new ConverterValueException(inValue, inType, targetType);
					}

				} else if (targetType == Boolean.class) {
					if (inType == Boolean.class) {
						return (T) inValue;
					} else if (typeIsNumber(inType)) {
						return (T) new Boolean((Double) inValue > 0);
					} else if (inType == String.class) {
						String inValStr = ((String) inValue).toUpperCase();
						return (T) new Boolean(inValStr == "1" || inValStr == "Y" || inValStr == "T" || inValStr == "TRUE" || inValStr == "ON");
					} else if (inType == Character.class) {
						char inValStr = Character.toUpperCase((Character)inValue);
						return (T) new Boolean(inValStr == '1' || inValStr == 'Y' || inValStr == 'T');
					} else {
						throw new ConverterValueException(inValue, inType, targetType);
					}
				} else if (typeIsNumber(targetType)) {
					if (typeIsNumber(inType)) {
						return (T) number2Number((Number)inValue, targetType);
					} else if (inType == Boolean.class) {
						return (T) new Integer(((Boolean) inValue) ? 1 : 0);
					} else if (inType == String.class) {
						return (T) string2Number((String)inValue, targetType);
					} else {
						throw new ConverterValueException(inValue, inType, targetType);
					}

				} else if (targetType == String.class) {
					return (T) inValue;
				}
			}

			return null;
		}
		return (T) inValue;
	}
}
