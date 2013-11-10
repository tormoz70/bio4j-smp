package bio4j.database.api;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public enum FieldType {
	
	STRING("string", String.class, null),
	FLOAT("float", BigDecimal.class, new Class<?>[] { BigDecimal.class, Double.class, Float.class }),
	CURRENCY("currency", BigDecimal.class, null),
	INT("int", Long.class, new Class<?>[] { Short.class, Integer.class, Long.class }),
	BOOLEAN("boolean", Boolean.class, null),
	DATE("date", Date.class, new Class<?>[] { Date.class, Calendar.class }),
	CLOB("clob", String.class, null),
	BLOB("blob", Byte[].class, null),
	OBJECT("object", Object.class, null),
    UNKNOWN("unknown", Object.class, null);
	
	private final String name;
	private final Class<?> type;
	private final Class<?>[] fromTypes;
	private FieldType(String name, Class<?> type, Class<?>[] fromTypes){
		this.name = name;
		this.type = type;
		this.fromTypes = fromTypes;
	}

}
