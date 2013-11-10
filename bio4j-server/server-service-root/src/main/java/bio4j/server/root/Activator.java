package bio4j.server.root;

import org.apache.log4j.xml.DOMConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio4j.server.api.BioEnvironment;
import bio4j.server.api.helpers.BioHelper;
import bio4j.server.api.services.BioService;

public class Activator implements BundleActivator {
	public static Logger LOG = LoggerFactory.getLogger(Activator.class);
	private void fireEvent_REGISTERED(BundleContext context, ServiceListener serviceListiner, String filter) throws InvalidSyntaxException {
		ServiceReference[] lst = context.getServiceReferences(null, filter);
		for (int i = 0; lst != null && i < lst.length; i++) {
			final ServiceReference sr = lst[i];
			serviceListiner.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, sr));
		}
	}

	public void start(BundleContext context) throws Exception {
		System.out.println(Activator.class+" - starting...");
		System.out.println(Logger.class+" - configuring...");
		DOMConfigurator.configure("log4j.xml");
		System.out.println(Logger.class+" - configured.");
		
		LOG.debug(BioEnvironment.class+" - initting...");
		EnvironmentImpl.getInstance().init();
		LOG.debug(BioEnvironment.class+" - inited.");
		
		BioServiceListener listener = new BioServiceListener(context);
		String serviceFilter = "(objectclass=bio4j.server.api.services.*)";
		context.addServiceListener(listener, serviceFilter);
		LOG.debug(BioServiceListener.class+" added as serviceFilter: " + serviceFilter);
		fireEvent_REGISTERED(context, listener, serviceFilter);
		LOG.debug(Activator.class+" - started.");
		System.out.println(Activator.class+" - started.");
	}

	public void stop(BundleContext context) throws Exception {
		LOG.debug(Activator.class+" - stopped.");
	}

	private class BioServiceListener implements ServiceListener {
		private BundleContext context;
		public BioServiceListener(BundleContext context){
			this.context = context;
		}
		public void serviceChanged(ServiceEvent serviceEvent) {
			//LOG.debug("Event serviceChanged fired"); 
			ServiceReference sr = serviceEvent.getServiceReference();
			final int eventType = serviceEvent.getType();
			switch (eventType) {
			case ServiceEvent.UNREGISTERING: {
				//LOG.debug("Event 'UNREGISTERING'(" + eventType + ") for service " + sr + " is  fired");
			}
				break;
			case ServiceEvent.REGISTERED: {
				//LOG.debug("Event 'REGISTERED' (" + eventType + ") for service " + sr + " is  fired");
				Object service = context.getService(sr);

				if(service instanceof BioService){
					BioService bioService = (BioService)service;
					EnvironmentImpl.getInstance().registerService(bioService);
					bioService.init(EnvironmentImpl.getInstance());
					//LOG.debug(sr + " is BioService. Inited.");
				} else if(service instanceof BioHelper){
					BioHelper bioHelper = (BioHelper)service;
						EnvironmentImpl.getInstance().registerHelper(bioHelper);
						//LOG.debug(sr + " is BioService. Inited.");
					}
			}
				break;
			default:
				break;
			}
		}
	};
}
