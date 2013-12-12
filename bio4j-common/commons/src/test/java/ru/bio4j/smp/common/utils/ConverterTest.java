package ru.bio4j.smp.common.utils;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Converter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

public class ConverterTest {

    @Test
    public void ConvertBigDecimal2Double() {
        try {
            BigDecimal inValue = new BigDecimal(123.654);
            Double actual = Converter.toType(inValue, Double.class);
            Double expected = 123.654;
            Assert.assertEquals(expected, actual);
        } catch (ConvertValueException ex) {
            Assert.fail(ex.getMessage());
        }
    }

	@Test
	public void ConvertString2Double1() {
		try {
			Double actual = Converter.toType("123.654", Double.class);
			Double expected = 123.654;
			Assert.assertEquals(expected, actual);
		} catch (ConvertValueException ex) {
			Assert.fail(ex.getMessage());
		}
	}
	@Test
	public void ConvertString2Double2() {
		try {
			Double actual = Converter.toType(" 123 123,654", Double.class);
			Double expected = 123123.654;
			Assert.assertEquals(expected, actual);
		} catch (ConvertValueException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void ConvertString2Float() {
		try {
			Object actual = Converter.toType(" 123 123,654", Float.class);
			Float expected = new Float(123123.654);
			Assert.assertEquals(expected, actual);
		} catch (ConvertValueException ex) {
			Assert.fail(ex.getMessage());
		}
	}

//    public static <T> T number2Number(Number inValue, Class<T> targetType) throws ParseException {
//    	try {
//    		T rslt = (T)inValue; // Как мне сделать, что бы тут возбуждалось исключение?
//    		return rslt; 
//    	} catch (Exception ex) {
//    		new ParseException(String.format("", inValue, targetType));
//    	}
//    	return null;
//    }
//	@Test
//	public void ConvertDouble2Integer() {
//		try {
//			Integer rslt = number2Number(new Double(123.2), Integer.class);
//		} catch (ParseException ex) {
//			Assert.assertTrue(true);
//		} catch (Exception ex) {
//			Assert.fail(ex.getMessage());
//		}
//	}
	
	@Test
	public void ConvertString2Integer() {
		try {
			Integer actual = Converter.toType("123,2", int.class);
			Integer expected = 123;
			Assert.assertEquals(expected, actual);
		} catch (ConvertValueException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	private void checkAType(Number value){
		try {
			Assert.assertEquals(new Byte((byte)12), Converter.toType(value, Byte.class));
			Assert.assertEquals(new Short((short)12), Converter.toType(value, Short.class));
			Assert.assertEquals(new Integer((int)12), Converter.toType(value, Integer.class));
			Assert.assertEquals(new Long((long)12), Converter.toType(value, Long.class));
			Assert.assertEquals(new Float((float)12), Converter.toType(value, Float.class));
			Assert.assertEquals(new Double((double)12), Converter.toType(value, Double.class));
		} catch (ConvertValueException ex) {
			Assert.fail(ex.getMessage());
		}
	}
	
	@Test
	public void ConvertNumber2Number() {
		checkAType((byte)12);
		checkAType((short)12);
		checkAType((int)12);
		checkAType((long)12);
		checkAType((float)12);
		checkAType((double)12);
	}
	
	@Test(enabled=true)
	public void typeIsNumber() {
        Double doubleValue = 1.2;
        Assert.assertTrue(doubleValue instanceof Number);

        Class<?> type = Double.class;
        Assert.assertTrue(Number.class.isAssignableFrom(type));
	}

    @Test(enabled=true)
    public void javaDate2sqlDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2012, (12-1), 20, 15, 11, 50);
        java.util.Date javadate = calendar.getTime();
        try {
            java.sql.Date sqldate = Converter.toType(javadate, java.sql.Date.class);
            Assert.assertEquals(sqldate.getTime(), javadate.getTime());
        } catch (ConvertValueException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test(enabled=true)
    public void sqlDate2javaDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2012, (12-1), 20, 15, 11, 50);
        java.sql.Date sqldate = new java.sql.Date(calendar.getTime().getTime());
        try {
            java.util.Date javadate = Converter.toType(sqldate, java.util.Date.class);
            Assert.assertEquals(javadate.getTime(), sqldate.getTime());
        } catch (ConvertValueException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test(enabled=true)
    public void sqlTimestamp2javaDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2012, (12-1), 20, 15, 11, 50);
        Timestamp sqldate = new Timestamp(calendar.getTime().getTime());
        try {
            java.util.Date javadate = Converter.toType(sqldate, java.util.Date.class);
            Assert.assertEquals(javadate.getTime(), sqldate.getTime());
        } catch (ConvertValueException ex) {
            Assert.fail(ex.getMessage());
        }
    }

}
