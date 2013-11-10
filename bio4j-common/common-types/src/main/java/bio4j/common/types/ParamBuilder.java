package bio4j.common.types;


public class ParamBuilder {

	private final String name;

	private Params owner;

	private Object value;
	private Object innerObject;
	private Class<?> type;
	private int size;
	private Direction direction;

	public ParamBuilder(String name) {
		this.name = name;
	}

	public static ParamBuilder copy(Param copyFrom) {
		ParamBuilder builder = new ParamBuilder(copyFrom.getName());
		builder.owner = copyFrom.getOwner();
		builder.value = copyFrom.getValue();
		builder.innerObject = copyFrom.getInnerObject();
		builder.type = copyFrom.getType();
		builder.size = copyFrom.getSize();
		builder.direction = copyFrom.getDirection();
		return builder;
	}
	
	public String getName() {
		return name;
	}

	public Params getOwner() {
		return owner;
	}

	public ParamBuilder setOwner(Params owner) {
		this.owner = owner;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public ParamBuilder setValue(Object value) {
		this.value = value;
		return this;
	}

	public Object getInnerObject() {
		return innerObject;
	}

	public ParamBuilder setInnerObject(Object innerObject) {
		this.innerObject = innerObject;
		return this;
	}

	public Class<?> getType() {
		return type;
	}

	public ParamBuilder setType(Class<?> type) {
		this.type = type;
		return this;
	}

	public int getSize() {
		return size;
	}

	public ParamBuilder setSize(int size) {
		this.size = size;
		return this;
	}

	public Direction getDirection() {
		return direction;
	}

	public ParamBuilder setDirection(Direction direction) {
		this.direction = direction;
		return this;
	}
	
	public Param build() {
		return new Param(this);
	}
}
