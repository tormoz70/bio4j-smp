package bio4j.server.common;

import java.lang.reflect.ParameterizedType;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio4j.server.api.helpers.BioHelper;
import bio4j.server.api.services.BioService;

public class BioHelperActivatorBase<T extends BioHelper> implements BundleActivator {
	public static Logger LOG;
	
	private ServiceRegistration serviceRegistration;
	private Class<? extends BioHelperBase> helperClass;

	public void start(BundleContext context) throws Exception {
		this.helperClass = (Class<? extends BioHelperBase>)((ParameterizedType)this.getClass().
			       getGenericSuperclass()).getActualTypeArguments()[0];
		LOG = LoggerFactory.getLogger(this.getClass());
		BioHelperBase newService = this.helperClass.newInstance();
		newService.setBundleContext(context);
		Class<?>[] intfs = newService.getClass().getInterfaces();
		for (Class<?> intf : intfs) 
			if(intf != BioService.class){
				newService.setHelperName(intf.getName());
				this.serviceRegistration = context.registerService(intf.getName(), newService, null);
				LOG.debug("Helper ["+this.helperClass.getName()+"] - registred as "+intf.getName());
				break;
			}
		LOG.debug("Helper ["+this.helperClass.getName()+"] - started");
	}

	public void stop(BundleContext context) throws Exception {
		if(this.serviceRegistration != null)
			this.serviceRegistration.unregister();
		LOG.debug("Helper ["+this.helperClass.getName()+"] - stopped");
	}

}
