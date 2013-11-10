package bio4j.database.direct.oracle.access;

public class sqlUtils {
	public class PackageName {
		public String pkgName;
		public String objName;
		public PackageName(String pkgName, String objName) {
			this.pkgName = pkgName;
			this.objName = objName;
		}
	}
	
    public static PackageName detectAutoDBMSParamsParts(String sql) {
    	return null;
//    	return new PackageName(
//    			RegexUtl.Find(sql, "\\b[\\w$]+\\b(?=[.])", true)
//    			,RegexUtl.Find(sql, "(?<=[.])\\b[\\w$]+\\b(?=\\s*[(;])", true)
//    	);
      }
	

}
