package ru.bio4j.smp.common.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Arrays;

/**
 * @author ayrat
 *	Конвертер типов
 */
public class Converter {

	public static boolean typeIsNumber(Class<?> type) {
        if(Arrays.asList(int.class, byte.class, short.class, long.class, float.class, double.class).contains(type))
            return true;
		return Number.class.isAssignableFrom(type);
	}

	private static Object wrapPrimitive(Object inValue) {
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

	private static Class<?> wrapPrimitiveType(Class<?> inType) {
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
		DecimalFormat fmt = new DecimalFormat("##############################.################");
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
	public static <T> T toType(Object inValue, Class<T> targetType) throws ConvertValueException {
		if ((targetType != null) && (inValue != null) && (inValue.getClass() == targetType))
			return (T) inValue;
		inValue = wrapPrimitive(inValue);
		if (targetType.isPrimitive()) {
			return (T) toType(inValue, wrapPrimitiveType(targetType));
		}
		Class<?> inType = (inValue == null) ? null : inValue.getClass();
		if (targetType != null) {
			if (inType != null) {
				if (targetType == java.util.Date.class) {
					if (inType == java.util.Date.class) {
						return (T) inValue;
                    } else if (inType == java.sql.Date.class) {
                        return (T) new java.util.Date(((java.sql.Date)inValue).getTime());
                    } else if (inType == java.sql.Timestamp.class) {
                        return (T) new java.util.Date(((java.sql.Timestamp)inValue).getTime());
					} else if (inType == String.class) {
						try {
							return (T) DateTimeParser.getInstance().pars((String) inValue);
						} catch (DateParseException ex) {
							throw new ConvertValueException(inValue, inType, targetType);
						}
					} else {
						throw new ConvertValueException(inValue, inType, targetType);
					}
                } else if (targetType == java.sql.Date.class) {
                    if (inType == java.sql.Date.class) {
                        return (T) inValue;
                    } else if (inType == java.util.Date.class) {
                        return (T) new java.sql.Date(((java.util.Date)inValue).getTime());
                    } else if (inType == java.sql.Timestamp.class) {
                        return (T) new java.sql.Date(((java.sql.Timestamp)inValue).getTime());
                    } else if (inType == String.class) {
                        java.util.Date javaDate;
                        try {
                            javaDate = DateTimeParser.getInstance().pars((String) inValue);
                        } catch (DateParseException ex) {
                            throw new ConvertValueException(inValue, inType, targetType);
                        }
                        return (T) toType(javaDate, java.sql.Date.class);
                    } else {
                        throw new ConvertValueException(inValue, inType, targetType);
                    }
                } else if (targetType == java.sql.Timestamp.class) {
                    if (inType == java.sql.Timestamp.class) {
                        return (T) inValue;
                    } else if (inType == java.util.Date.class) {
                        return (T) new java.sql.Timestamp(((java.util.Date)inValue).getTime());
                    } else if (inType == java.sql.Date.class) {
                        return (T) new java.sql.Timestamp(((java.sql.Date)inValue).getTime());
                    } else if (inType == String.class) {
                        java.util.Date javaDate;
                        try {
                            javaDate = DateTimeParser.getInstance().pars((String) inValue);
                        } catch (DateParseException ex) {
                            throw new ConvertValueException(inValue, inType, targetType);
                        }
                        return (T) toType(javaDate, java.sql.Date.class);
                    } else {
                        throw new ConvertValueException(inValue, inType, targetType);
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
						throw new ConvertValueException(inValue, inType, targetType);
					}
				} else if (typeIsNumber(targetType)) {
					if (typeIsNumber(inType)) {
						return number2Number((Number)inValue, targetType);
					} else if (inType == Boolean.class) {
						return (T) new Integer(((Boolean) inValue) ? 1 : 0);
					} else if (inType == String.class) {
						return string2Number((String)inValue, targetType);
					} else {
						throw new ConvertValueException(inValue, inType, targetType);
					}

				} else if (targetType == String.class) {
					return (T) inValue;
                } else if (targetType == byte[].class) {
                    if (inType == String.class)
                        return (T)((String)inValue).getBytes();
				}
			}

			return null;
		}
		return (T) inValue;
	}

}
