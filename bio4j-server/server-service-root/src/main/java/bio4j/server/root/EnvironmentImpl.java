package bio4j.server.root;

import java.lang.reflect.Field;
import java.util.Hashtable;

import bio4j.common.utils.Utl;
import bio4j.server.api.BioEnvironment;
import bio4j.server.api.helpers.BioHelper;
import bio4j.server.api.helpers.InjectHelper;
import bio4j.server.api.services.BioConfigService;
import bio4j.server.api.services.BioService;
import bio4j.server.api.services.DatabaseProvider;
import bio4j.server.api.services.SessionProvider;
import bio4j.server.api.services.UserAuthService;
import bio4j.server.model.BioConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EnvironmentImpl implements BioEnvironment {
    public static Logger LOG = LoggerFactory.getLogger(EnvironmentImpl.class);

	private final static BioEnvironment instance = new EnvironmentImpl();  
	
	public static BioEnvironment getInstance(){
		return instance;
	} 
	
	@Override
	public String getSystemName() {
		return "Bio4j";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	private static Boolean checkHelperType(Class<?> fieldType, Class<?> helperType) {
        Boolean result = Utl.typesIsSame(fieldType, helperType);
        LOG.debug("Compare helper type ["+fieldType+"] ? ["+helperType+"] = "+result);
        return result;
	}
	
	private static void refreshServiceHelperInjection(BioService service, BioHelper helper) {
        LOG.debug("start...");
        LOG.debug(" -- service: "+service);
        LOG.debug(" -- helper: "+helper);
		Field[] fields = service.getClass().getDeclaredFields();
		for(Field field : fields){
            LOG.debug(" -- -- field: "+field.getName());
			if(field.getAnnotation(InjectHelper.class) != null) {
                LOG.debug(" -- -- -- has InjectHelper annotation!");
				if(checkHelperType(field.getType(), helper.getClass()))
					try {
						field.set(service, helper);
					} catch (IllegalAccessException ex) {
                        LOG.error(ex.getMessage(), ex);
					}
			}
		}
        LOG.debug("end.");
	}
	
	private void refreshAllServicesHelperInjection(BioService service) {
		for(BioHelper helper : this.helperRegistry.values()) {
			refreshServiceHelperInjection(service, helper);
		}
	}
	private void refreshAllServicesHelperInjection(BioHelper helper) {
		for(BioService service : this.serviceRegistry.values()) {
			refreshServiceHelperInjection(service, helper);
		}
	}
	
	private Hashtable<String, BioService> serviceRegistry = new Hashtable<String, BioService>();
	
	@Override
	public void registerService(BioService service) {
        if(service.getServiceName() == null)
            LOG.error("Service " + service + "has no name!");
		this.serviceRegistry.put(service.getServiceName(), service);
		this.refreshAllServicesHelperInjection(service);
		if(service instanceof SessionProvider)
			this.sessionProvider = (SessionProvider)service;
		if(service instanceof UserAuthService)
			this.userAuthService = (UserAuthService)service;
		if(service instanceof DatabaseProvider)
			this.databaseProvider = (DatabaseProvider)service;
		if(service instanceof BioConfigService)
			this.configService = (BioConfigService)service;
		
	}

	private Hashtable<String, BioHelper> helperRegistry = new Hashtable<String, BioHelper>();

	@Override
	public void registerHelper(BioHelper helper) {
		this.helperRegistry.put(helper.getHelperName(), helper);
		this.refreshAllServicesHelperInjection(helper);
		
	}
	
	@Override
	public BioService getService(String serviceName) {
		if(this.serviceRegistry.containsKey(serviceName))
			return this.serviceRegistry.get(serviceName);
		return null;
	}
	
	private BioConfigService configService;
	@Override
	public BioConfig getConfig() {
		if(this.configService != null)
			return this.configService.getCurrentConfig();
		return null;
	}

	private SessionProvider sessionProvider;
	@Override
	public SessionProvider getSessionProvider() {
		return this.sessionProvider;
	}

	private UserAuthService userAuthService;
	@Override
	public UserAuthService getUserAuthService() {
		return this.userAuthService;
	}
	
	private DatabaseProvider databaseProvider;
	@Override
	public DatabaseProvider getDatabaseProvider() {
		return this.databaseProvider;
	}

	
}
