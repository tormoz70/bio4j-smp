package bio4j.transport.packets;

import bio4j.common.types.Params;

public class JStoreFilterItem implements Cloneable {

    /** Инвертировать условие */
    private Boolean not;
	public Boolean getNot() { return not; }
	public void setNot(Boolean not) { this.not = not; }
    
	/** Имя поля */
    private String fieldName;
    public String getFieldName() { return fieldName; }
	public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    
	/** Тип поля */
    private JSFieldType fieldType;
    public JSFieldType getFieldType() { return fieldType; }
	public void setFieldType(JSFieldType fieldType) { this.fieldType = fieldType; }
    
	/** Сравниваемое значение */
    private Object fieldValue;
    public Object getFieldValue() { return fieldValue; }
	public void setFieldValue(Object fieldValue) { this.fieldValue = fieldValue; }
    
	/** Оператор сравнения */
    private ComparisionOperator cmpOperator;
    public ComparisionOperator getCmpOperator() { return cmpOperator; }
	public void setCmpOperator(ComparisionOperator cmpOperator) { this.cmpOperator = cmpOperator; }

    private String detectSQLFormat(Boolean hasNot, ComparisionOperator operation, JSFieldType jsFieldType) {
        String rslt = null;
        switch (jsFieldType) {
          case String: 
          case Clob: {
              switch (operation) {
                case Eq: 		rslt = "UPPER(%s) =  UPPER(:%s)"; break;
                case Gt: 		rslt = "UPPER(%s) >  UPPER(:%s)"; break;
                case Ge: 		rslt = "UPPER(%s) >= UPPER(:%s)"; break;
                case Lt: 		rslt = "UPPER(%s) <  UPPER(:%s)"; break;
                case Le: 		rslt = "UPPER(%s) <= UPPER(:%s)"; break;
                case Bgn: 		rslt = "UPPER(%s) LIKE UPPER(:%s||'%%')"; break;
                case End: 		rslt = "UPPER(%s) LIKE UPPER('%%'||:%s)"; break;
                case In: 		rslt = "UPPER(%s) LIKE UPPER('%%'||:%s||'%%')"; break;
                case IsNull: 	rslt = "%s IS NULL"; break;
              }
            } break;
          case Boolean: {
              rslt = "%s = :%s";
            } break;
          case Float:
          case Int:
          case Blob:
          case Object: {
              switch (operation) {
                case Eq: rslt = "%s =  :%s"; break;
                case Gt: rslt = "%s >  :%s"; break;
                case Ge: rslt = "%s >= :%s"; break;
                case Lt: rslt = "%s <  :%s"; break;
                case Le: rslt = "%s <= :%s"; break;
                default: break;
              }
            } break;
          case Date: {
              switch (operation) {
                case Eq: rslt = "%s =  :%s"; break;
                case Gt: rslt = "%s >  :%s"; break;
                case Ge: rslt = "%s >= :%s"; break;
                case Lt: rslt = "%s <  :%s"; break;
                case Le: rslt = "%s <= :%s"; break;
                default: break;
              }
            } break;
          default: break;
        }
        return (hasNot) ? String.format("NOT(%)", rslt) : rslt;
      }

    private JSFieldType _detectFTypeGranted() {
        JSFieldType ftype = JSFieldType.String;
        if (this.getFieldType() != null)
          ftype = this.getFieldType();
        else {
          if (this.getFieldValue() != null)
            ftype = null; //ftypeHelper.ConvertTypeToFType(this.FieldValue.GetType());
        }
        return ftype;
    }
    
      public String buildSQLCondition(String sql, Params prms) {
//        if (prms == null)
//          throw new ArgumentNullException("prms");
//        var v_val_param_name = this.getFieldName() + "$afilter";
//        var v_ftype = this._detectFTypeGranted();
//        sql = String.Format(this.detectSQLFormat(this.Not, this.CmpOperator, v_ftype), this.FieldName, v_val_param_name);
//        var v_ptype = ftypeHelper.ConvertFTypeToType(v_ftype);
//        Object v_pval;
//        if (v_ftype == JSFieldType.Boolean) {
//          v_ptype = typeof(Int64);
//          var v_bool = (Boolean)this.FieldValue;
//          v_pval = (v_bool) ? 1 : 0;
//        }else
//          v_pval = this.FieldValue;
//        prms.Add(new Param { 
//          Name = v_val_param_name,
//          ParamType = v_ptype,
//          Value = v_pval
//        });
    	  return null;
      }
	
	
}
