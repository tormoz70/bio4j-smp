package bio4j.server.common;

import java.lang.reflect.ParameterizedType;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio4j.server.api.services.BioService;

public class BioServiceActivatorBase<T extends BioService> implements BundleActivator {
	public static Logger LOG;
	
	private ServiceRegistration serviceRegistration;
	private Class<? extends BioServiceBase> serviceClass;

	public void start(BundleContext context) throws Exception {
		this.serviceClass = (Class<? extends BioServiceBase>)((ParameterizedType)this.getClass().
			       getGenericSuperclass()).getActualTypeArguments()[0];
		LOG = LoggerFactory.getLogger(this.getClass());
		BioServiceBase newService = this.serviceClass.newInstance();
		newService.setBundleContext(context);
		Class<?>[] intfs = newService.getClass().getInterfaces();
		for (Class<?> intf : intfs) 
			if(intf != BioService.class){
				newService.setServiceName(intf.getName());
				this.serviceRegistration = context.registerService(intf.getName(), newService, null);
				LOG.debug("Service ["+this.serviceClass.getName()+"] - registred as "+intf.getName());
				break;
			}
		LOG.debug("Service ["+this.serviceClass.getName()+"] - started");
	}

	public void stop(BundleContext context) throws Exception {
		if(this.serviceRegistration != null)
			this.serviceRegistration.unregister();
		LOG.debug("Service ["+this.serviceClass.getName()+"] - stopped");
	}

}
