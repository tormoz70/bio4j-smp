package bio4j.transport.packets;

public enum JStoreSortOrder {
    None(0, "NONE"),
    Asc(1, "ASC"),
    Desc(2, "DESC");

    private final int code;
    private final String value;
    private JStoreSortOrder(int code, String value) {
    	this.code = code;
    	this.value = value; 
    }
	public int getCode() {
	    return code;
    }
	public String getValue() {
	    return value;
    }
	
}
