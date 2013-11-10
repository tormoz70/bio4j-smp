package bio4j.common.types;

import org.apache.commons.beanutils.BeanUtils;

import bio4j.common.utils.StringUtl;

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
		return ParamBuilder.copy(this).setOwner(destOwner).build();
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public String getValueAsString() {
		return (this.value == null) ? null : this.value.toString();
	}

	public Object getInnerObject() {
		return this.innerObject;
	}

	public Class<?> getType() {
		return this.type;
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
			objsStr = StringUtl.appendStr(objsStr, innrObjStr, ";");
		if (!StringUtl.isNullOrEmpty(objsStr))
			objsStr = "(" + objsStr + ")";
		String valStr = this.getValue() + objsStr;
		return String.format("(%s=[%s]; tp:%s; sz:%d; dr:%s)", this.getName(), valStr, this.getType(), this.getSize(), this.getDirection());
	}

	@Override
    public Param clone() throws CloneNotSupportedException {
		ParamBuilder builder = ParamBuilder.copy(this);
		try {
			builder.setValue(BeanUtils.cloneBean(this.getValue()));
		} catch(Exception ex) {
			builder.setValue(this.getValue());
		}
		try {
			builder.setInnerObject(BeanUtils.cloneBean(this.getInnerObject()));
		} catch(Exception ex) {
			builder.setInnerObject(this.getInnerObject());
		}
	    return builder.build();
    }
	
}
