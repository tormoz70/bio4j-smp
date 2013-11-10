package bio4j.transport.packets;

public enum ComparisionOperator {
    Eq(0, "Равно"),
    Lt(1, "Меньше"),
    Le(2, "Меньше или равно"),
    Gt(3, "Больше"),
    Ge(4, "Больше или равно"),
    Bgn(5, "Начинается с"),
    End(6, "Оканчивается на"),
    In(7, "Содержит"),
    IsNull(8, "Пусто");
    
    private final int code;
    private final String description;
    private ComparisionOperator(int code, String description) {
    	this.code = code;
    	this.description = description;
    }
	public int getCode() {
	    return code;
    }
	public String getDescription() {
	    return description;
    }
}
