package bio4j.server.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

import bio4j.server.api.BioEnvironment;
import bio4j.server.api.Delegate;
import bio4j.server.api.services.BioService;



public abstract class BioServiceBase implements BioService {

	private BioEnvironment environment;
	private BundleContext bundleContext;
	private String serviceName;
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	@Override
	public void init(BioEnvironment environment) {
		this.environment = environment; 
		this.doOnInit();
	}

	@Override
	public BioEnvironment getEnvironment() {
		return this.environment;
	}

	@Override
	public BundleContext getBundleContext() {
		return this.bundleContext;
	}
	@Override
	public String getServiceName() {
		return this.serviceName;
	}
	
	protected void doOnInit() {
	}
	
}
