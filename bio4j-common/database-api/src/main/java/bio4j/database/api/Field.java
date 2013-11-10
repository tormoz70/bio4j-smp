package bio4j.database.api;

import java.math.BigDecimal;
import java.util.Date;

public interface Field {

	String getName();

	FieldType getDataType();

	Class<?> getJavaType();
	
	Long getId();
	
	String getCaption();

	String getPkIndex();
	
	Boolean isDBNull();
	
	Object getObject();
	
	Date getDate();
	
	Boolean getBoolean();
	
	Long getLong();
	
	BigDecimal getBigDecimal();

	Double getDouble();
	
	String getString();
}
