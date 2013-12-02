package bio4j.common.utils;

import flexjson.JSONContext;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;

public class ExceptionTransformer extends AbstractTransformer {

	public void transform(Object value) {
        JSONContext context = getContext();
		String valueStr = new JSONSerializer().exclude("cause", "localizedMessage", "stackTraceDepth").include("stackTrace").serialize(value);
		context.write(valueStr);
	}

}
