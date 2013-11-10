package bio4j.transport.packets;


public enum JSFieldType {
    String	(0, "string", 	String.class, 			null),
    Float	(1, "float", 	Double.class, 			new Class<?>[] { Double.class, Float.class }),
    Int		(2, "int", 		Long.class, 			new Class<?>[] { Short.class, Integer.class, Long.class }),
    Boolean	(3, "boolean", 	Boolean.class, 			null),
    Date	(4, "date", 	java.util.Date.class, 	new Class<?>[] { java.util.Date.class, java.sql.Date.class }),
    Clob	(6, "clob", 	String.class, 			null),
    Object	(7, "object", 	Object.class, 			null),
    Blob	(8, "blob", 	Byte[].class, 			null),
    Currency(9, "currency", Double.class, 			null);

    private final int code;
    private final String name;
    private final Class<?> toType;
    private final Class<?>[] fromTypes;
    private JSFieldType(int code, String name, Class<?> toType, Class<?>[] fromTypes) {
        this.code = code;
        this.name = name;
        this.toType = toType;
        this.fromTypes = fromTypes;
    }
	public int getCode() {
	    return code;
    }
	public String getName() {
	    return name;
    }
	public Class<?> getToType() {
	    return toType;
    }
	public Class<?>[] getFromTypes() {
	    return fromTypes;
    }
	
    public static String ConvertTypeToStr(Class<?> clazz) {

//        if (type != null) {
//          foreach (var fi in typeof(JSFieldType).GetFields()) {
//            var attr = enumHelper.GetAttributeByInfo<MappingAttribute>(fi);
//            if (attr != null) {
//              if (((attr.FromNetTypes != null) && attr.FromNetTypes.Any(t => t.Equals(type))) ||
//                  ((attr.FromNetTypes == null) && (attr.ToNetType.Equals(type))))
//                return attr.XmlName;
//            }
//          }
//        }
//        throw new ArgumentException("Невозможно преобразовать тип \"" + type + "\".", "type");
    	return null;
      }

    public static JSFieldType ConvertTypeToFType(Class<?> clazz) {
//        return ConvertStrToFieldType(ConvertTypeToStr(type));
    	return null;
    }

    public static Class<?> ConvertFTypeToType(JSFieldType type) {
//        return enumHelper.GetAttributeByValue<MappingAttribute>(type).ToNetType;
    	return null;
    }
    
}
