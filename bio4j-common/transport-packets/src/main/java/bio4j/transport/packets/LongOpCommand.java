package bio4j.transport.packets;

public enum LongOpCommand {
	Run(0), Break(1), Kill(2), GetState(3), GetResult(4);

	private final int code;

	private LongOpCommand(int code){
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
	
}
