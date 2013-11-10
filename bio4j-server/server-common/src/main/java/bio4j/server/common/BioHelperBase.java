package bio4j.server.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

import bio4j.server.api.BioEnvironment;
import bio4j.server.api.Delegate;
import bio4j.server.api.helpers.BioHelper;
import bio4j.server.api.services.BioService;



public abstract class BioHelperBase implements BioHelper {

	private BioEnvironment environment;
	private BundleContext bundleContext;
	private String helperName;
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	public void setHelperName(String helperName) {
		this.helperName = helperName;
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
	public String getHelperName() {
		return this.helperName;
	}
	
	protected void doOnInit() {
	}
	
}
