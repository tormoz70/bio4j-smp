package bio4j.database.direct.oracle.access;

import bio4j.database.api.Field;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 02.12.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class FieldImpl implements Field {
    private String name;
    private Class<?> type;
    private int dataType;
    private int id;

    private Object value;

    private String caption;
    private int pkIndex;

    public FieldImpl(Class<?> type, int id, String name, int dataType) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.dataType = dataType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getDataType() {
        return this.dataType;
    }

    @Override
    public Class<?> getJavaType() {
        return this.type;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    @Override
    public void setCaption(String value) {
        this.caption = value;
    }

    @Override
    public int getPkIndex() {
        return this.pkIndex;
    }

    @Override
    public void setPkIndex(int value) {
        this.pkIndex = value;
    }

    @Override
    public Boolean isDBNull() {
        return this.value == null;
    }

    @Override
    public <T> T getValue() {
        return (T)this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}
