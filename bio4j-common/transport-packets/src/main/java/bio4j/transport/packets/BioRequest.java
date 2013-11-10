package bio4j.transport.packets;

import bio4j.common.types.Params;

public class BioRequest implements Cloneable {

    /** Тип запроса */
    private BioRequestType requestType;
	public BioRequestType getRequestType() { return requestType; }
	public void setRequestType(BioRequestType requestType) { this.requestType = requestType; }
	
    /** Код запрашиваемого инф. объекта */
    private String bioCode;
	public String getBioCode() { return bioCode; }
	public void setBioCode(String bioCode) { this.bioCode = bioCode; }

    /** параметры информационного объекта */
    private Params bioParams;
	public Params getBioParams() { return bioParams; }
	public void setBioParams(Params bioParams) { this.bioParams = bioParams; }
	
	@Override
    public BioRequest clone() throws CloneNotSupportedException {
		BioRequest result = new BioRequest();
		result.setRequestType(this.getRequestType());
		result.setBioCode(this.getBioCode());
		result.setBioParams((this.getBioParams() != null) ? this.getBioParams().clone() : null);
		return result;
    }


}
