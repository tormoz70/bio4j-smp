package bio4j.database.api;

public interface Field {

	String getName();

	int getDataType();

	Class<?> getJavaType();

	Integer getId();
	
	String getCaption();

    void setCaption(String value);

	int getPkIndex();

    void setPkIndex(int value);

	Boolean isDBNull();
	
	<T> T getValue();

    void setValue(Object value);
}
