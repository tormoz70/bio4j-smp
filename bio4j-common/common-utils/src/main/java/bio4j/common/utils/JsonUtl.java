package bio4j.common.utils;

import java.lang.reflect.Type;
import java.util.Date;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.DateTransformer;

public class JsonUtl {
	private static String csDateTimeFormat = "yyyy.MM.dd-HH:mm:ss";

	public static String encode(Object object) {
		JSONSerializer serializer = new JSONSerializer();
		return serializer
				.transform(new DateTransformer(csDateTimeFormat), Date.class)
				.transform(new ExceptionTransformer(), Exception.class)
				.deepSerialize(object);
	}

	public static Object decode(String json) {
		JSONDeserializer<Object> deserializer = new JSONDeserializer<Object>().use(Date.class, new ObjectFactory() {
			@Override
			public Object instantiate(ObjectBinder context, Object value, Type targetType, @SuppressWarnings("rawtypes") Class targetClass) {
				return DateUtl.parse((String)value, csDateTimeFormat);
			}
		});
		return deserializer.deserialize(json);
	}
	public static <T> T decode(Class<T> clazz, String json) {
		JSONDeserializer<T> deserializer = new JSONDeserializer<T>().use(Date.class, new ObjectFactory() {
			@Override
			public Object instantiate(ObjectBinder context, Object value, Type targetType, @SuppressWarnings("rawtypes") Class targetClass) {
				return DateUtl.parse((String)value, csDateTimeFormat);
			}
		});
		return deserializer.deserialize(json);
	}
}
