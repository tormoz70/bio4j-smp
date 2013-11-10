package bio4j.server.api;

import bio4j.server.api.helpers.BioHelper;
import bio4j.server.api.services.BioConfigService;
import bio4j.server.api.services.BioService;
import bio4j.server.api.services.DatabaseProvider;
import bio4j.server.api.services.SessionProvider;
import bio4j.server.api.services.UserAuthService;
import bio4j.server.model.BioConfig;


public interface BioEnvironment {
	void init();
	String getSystemName();
	void registerService(BioService service);
	void registerHelper(BioHelper helper);
	BioConfig getConfig();
	SessionProvider getSessionProvider();
	UserAuthService getUserAuthService();
	DatabaseProvider getDatabaseProvider();
	BioService getService(String serviceName);
	
}
