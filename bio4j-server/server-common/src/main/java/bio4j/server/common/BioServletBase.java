package bio4j.server.common;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import bio4j.server.api.BioEnvironment;
import bio4j.server.api.services.BioService;
import bio4j.server.api.services.BioSrvcService;

public abstract class BioServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BioService service;
	
	@Override
	public void init() {
		// Инициализируем ссылку на свой собственный сервис
		BundleContext bundleContext = (BundleContext) getServletContext().getAttribute("osgi-bundlecontext");
		this.environment = null;
		ServiceReference sr = bundleContext.getServiceReference(BioSrvcService.class.getName());
		if(sr != null) 
			this.service = (BioService)bundleContext.getService(sr);
		// Инициализируем ссылку окружение
		if(this.service != null)
			this.environment = this.service.getEnvironment();
	}

	private BioEnvironment environment;
	
	protected BioEnvironment getEnvironment() { return this.environment; }
	
	protected abstract void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException;

	protected abstract void processRequestError(Exception ex, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException;
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {
			if(this.environment == null)
				throw new Exception("BioEnvironment not correctly inited!");
			this.processRequest(request, response);
		} catch(Exception ex) {
			this.processRequestError(ex, request, response);
		}
	}
}
