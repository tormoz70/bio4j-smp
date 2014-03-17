package ru.bio4j.smp.database.api;

public interface Field {

	String getName();

	int getSqlType();

	Class<?> getType();

	Integer getId();
	
	String getCaption();

    void setCaption(String value);

	int getPkIndex();

    void setPkIndex(int value);

}
