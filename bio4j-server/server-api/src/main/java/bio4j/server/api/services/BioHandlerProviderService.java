package bio4j.server.api.services;

public interface BioHandlerProviderService extends BioService {
	void registerHandler(String mapping, Class<?> handlerType);
}
