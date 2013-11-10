package bio4j.transport.packets;

import java.util.HashMap;

import bio4j.common.utils.StringUtl;

public class JStoreSort extends HashMap<String, JStoreSortOrder> {

	private static final long serialVersionUID = 1L;

    public String getSQL() {
        String rslt = null;
        for (String key : this.keySet()) {
        	rslt = StringUtl.appendStr(rslt, String.format("%s %s", key, this.get(key).getValue()), ",");
        }
        return rslt;
      }
	
	@Override
	public JStoreSort clone() {
		JStoreSort result = new JStoreSort();
		result.putAll(this); 
		return result;
	}
}
