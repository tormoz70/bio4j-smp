package bio4j.server.api.helpers;

public interface RegisterHandlerDelegate {
	void action(String handlerName, Class<?> handlerType);
}
