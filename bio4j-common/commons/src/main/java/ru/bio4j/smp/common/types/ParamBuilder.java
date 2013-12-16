package ru.bio4j.smp.common.types;


public class ParamBuilder {

	private String name = null;

	private Params owner = null;

	private Object value = null;
	private Object innerObject = null;
	private Class<?> type = Object.class;
	private int size = 0;
	private Direction direction = Direction.Input;

	public ParamBuilder(Params owner) {
        this.owner = owner;
	}

	public static Param copy(Param copyFrom, Params dest) {
        return new ParamBuilder(dest)
            .name(copyFrom.getName())
		    .value(copyFrom.getValue())
		    .innerObject(copyFrom.getInnerObject())
		    .type(copyFrom.getType())
		    .size(copyFrom.getSize())
		    .direction(copyFrom.getDirection())
            .build();
	}

    public static ParamBuilder override(Param param, Params dest) {
        return new ParamBuilder(dest)
            .name(param.getName())
            .value(param.getValue())
            .innerObject(param.getInnerObject())
            .type(param.getType())
            .size(param.getSize())
            .direction(param.getDirection());
    }

	public String getName() {
		return this.name;
	}

    public ParamBuilder name(String name) {
        this.name = name;
        return this;
    }

	public Params getOwner() {
		return this.owner;
	}

	public Object getValue() {
		return this.value;
	}

	public ParamBuilder value(Object value) {
		this.value = value;
		return this;
	}

	public Object getInnerObject() {
		return this.innerObject;
	}

	public ParamBuilder innerObject(Object innerObject) {
		this.innerObject = innerObject;
		return this;
	}

	public Class<?> getType() {
		return this.type;
	}

	public ParamBuilder type(Class<?> type) {
		this.type = type;
		return this;
	}

	public int getSize() {
		return this.size;
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
