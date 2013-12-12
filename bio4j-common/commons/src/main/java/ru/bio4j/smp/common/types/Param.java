package ru.bio4j.smp.common.types;

import org.apache.commons.beanutils.BeanUtils;

import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Converter;
import ru.bio4j.smp.common.utils.StringUtl;

import java.sql.Types;

public class Param implements Cloneable {

	private final Params owner;
	private final String name;

	private final Object value;
	private final Object innerObject;
	private final Class<?> type;
	private final  int size;
	private final  Direction direction;

	public Param(ParamBuilder builder) {
		this.name = builder.getName();
		this.owner = builder.getOwner();
		this.value = builder.getValue();
		this.innerObject = builder.getInnerObject();
		this.type = builder.getType();
		this.size = builder.getSize();
		this.direction = builder.getDirection();
	}

	public Params getOwner() {
		return this.owner;
	}

	protected Param export(Params destOwner) {
		return ParamBuilder.override(this).owner(destOwner).build();
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

    public <T> T getValue(Class<T> type) throws ConvertValueException {
        return Converter.toType(this.value, type);
    }

	public String getValueAsString() {
        try{
		    return (this.value == null) ? null : Converter.toType(this.value, String.class);
        } catch (ConvertValueException ex) {
            return ex.getMessage();
        }
	}

	public Object getInnerObject() {
		return this.innerObject;
	}

	public Class<?> getType() {
		return this.type;
	}

    public int getSqlType() {
        int stringSize = 0;
        if(this.getType() == String.class){
            if(((this.getDirection() == Direction.InputOutput) || (this.getDirection() == Direction.Input)) && (stringSize == 0))
                stringSize = StringUtl.isNullOrEmpty(this.getValueAsString()) ? 0 : this.getValueAsString().length();
        }
        boolean isCallable = (this.getDirection() == Direction.InputOutput) || (this.getDirection() == Direction.Output);
        return this.owner.getSqlTypeConverter().write(this.getType(), stringSize, isCallable);
    }

	public int getSize() {
		return this.size;
	}

	public Direction getDirection() {
		return this.direction;
	}

//	public synchronized void remove() {
//		if (this.getOwner() != null) {
//			this.getOwner().remove(this);
//		}
//	}
//
	public String toString() {
		String innrObjStr = (this.getInnerObject() == null) ? null : "o:" + this.getInnerObject().toString();
		String objsStr = null;
		if (StringUtl.isNullOrEmpty(innrObjStr))
			objsStr = StringUtl.append(objsStr, innrObjStr, ";");
		if (!StringUtl.isNullOrEmpty(objsStr))
			objsStr = "(" + objsStr + ")";
		String valStr = this.getValue() + objsStr;
		return String.format("(%s=[%s]; tp:%s; sz:%d; dr:%s)", this.getName(), valStr, this.getType(), this.getSize(), this.getDirection());
	}

	@Override
    public Param clone() throws CloneNotSupportedException {
		ParamBuilder builder = ParamBuilder.override(this);
		try {
			builder.value(BeanUtils.cloneBean(this.getValue()));
		} catch(Exception ex) {
			builder.value(this.getValue());
		}
		try {
			builder.innerObject(BeanUtils.cloneBean(this.getInnerObject()));
		} catch(Exception ex) {
			builder.innerObject(this.getInnerObject());
		}
	    return builder.build();
    }
	
}
