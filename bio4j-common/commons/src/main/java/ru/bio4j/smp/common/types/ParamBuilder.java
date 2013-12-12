package ru.bio4j.smp.common.types;


public class ParamBuilder {

	private String name;

	private Params owner;

	private Object value;
	private Object innerObject;
	private Class<?> type;
	private int size;
	private Direction direction;

	public ParamBuilder() {
	}

	public static Param copy(Param copyFrom) {
        return new ParamBuilder()
            .name(copyFrom.getName())
		    .owner(copyFrom.getOwner())
		    .value(copyFrom.getValue())
		    .innerObject(copyFrom.getInnerObject())
		    .type(copyFrom.getType())
		    .size(copyFrom.getSize())
		    .direction(copyFrom.getDirection())
            .build();
	}

    public static ParamBuilder override(Param param) {
        return new ParamBuilder()
                .name(param.getName())
                .owner(param.getOwner())
                .value(param.getValue())
                .innerObject(param.getInnerObject())
                .type(param.getType())
                .size(param.getSize())
                .direction(param.getDirection());
    }

	public String getName() {
		return name;
	}

    public ParamBuilder name(String name) {
        this.name = name;
        return this;
    }

	public Params getOwner() {
		return owner;
	}

	public ParamBuilder owner(Params owner) {
		this.owner = owner;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public ParamBuilder value(Object value) {
		this.value = value;
		return this;
	}

	public Object getInnerObject() {
		return innerObject;
	}

	public ParamBuilder innerObject(Object innerObject) {
		this.innerObject = innerObject;
		return this;
	}

	public Class<?> getType() {
		return type;
	}

	public ParamBuilder type(Class<?> type) {
		this.type = type;
		return this;
	}

	public int getSize() {
		return size;
	}

	public ParamBuilder size(int size) {
		this.size = size;
		return this;
	}

	public Direction getDirection() {
		return direction;
	}

	public ParamBuilder direction(Direction direction) {
		this.direction = direction;
		return this;
	}
	
	public Param build() {
		return new Param(this);
	}

}
