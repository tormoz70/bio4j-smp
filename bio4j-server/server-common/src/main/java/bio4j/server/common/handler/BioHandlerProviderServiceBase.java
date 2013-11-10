package bio4j.server.common.handler;

import org.slf4j.Logger;

import bio4j.server.api.helpers.InjectHelper;
import bio4j.server.common.BioServiceBase;
import bio4j.server.api.helpers.BioHandlerMapper;
import bio4j.server.api.helpers.RegisterHandlerDelegate;
import bio4j.server.model.BioConfig;

public class BioHandlerProviderServiceBase extends BioServiceBase {
	private static Logger LOG;
	private BioConfig config;

	public void registerHandler(String mapping, Class<?> handlerType) {
		
	}
	
	@InjectHelper
	private BioHandlerMapper helper;
	
	@Override
	protected void doOnInit() {
		this.helper.doScanServicePackage(this.getClass(), this.getBundleContext().getBundle(), new RegisterHandlerDelegate() {
			@Override
			public void action(String handlerName, Class<?> handlerType) {
				BioHandlerProviderServiceBase.this.registerHandler(handlerName, handlerType);
			}
		});
	}

	public BioConfig getCurrentConfig() {
		return this.config;
	}
}
