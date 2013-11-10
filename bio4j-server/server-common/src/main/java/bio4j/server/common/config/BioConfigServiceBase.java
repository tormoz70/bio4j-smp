package bio4j.server.common.config;

import java.io.InputStream;
import java.lang.annotation.Annotation;

import javax.xml.bind.JAXBException;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio4j.common.utils.StringUtl;
import bio4j.common.utils.Utl;
import bio4j.server.api.services.BioConfigService;
import bio4j.server.common.BioServiceBase;
import bio4j.server.model.BioConfig;

public class BioConfigServiceBase extends BioServiceBase {
	private static Logger LOG;
	private BioConfig config;

	private static String getConfigPathFromAnnotation(Class<?> clazz) {
		BioConfigProps cfgProps = Utl.findAnnotation(BioConfigProps.class, clazz);
		if(cfgProps != null)
			return cfgProps.path();
		return null;
	}
	
	@Override
	protected void doOnInit() {
		Class<?> clazz = this.getClass();
		Bundle bundle = this.getBundleContext().getBundle();
		LOG = LoggerFactory.getLogger(clazz);
		LOG.debug("doOnInit - starts...");
		if(bundle != null) {
			String cfgDocPath = getConfigPathFromAnnotation(clazz);
			LOG.debug("cfgDocPath of type {" + clazz + "} detected: "+cfgDocPath);
			if(!StringUtl.isNullOrEmpty(cfgDocPath)){
				InputStream configIn = clazz.getClassLoader().getResourceAsStream(cfgDocPath);
				if(configIn != null){
					try {
						BioConfig cfg = Utl.unmarshalXml(BioConfig.class, configIn);
						LOG.debug("config loaded: ");
						LOG.debug(" --- ApplicationTitle: " + cfg.getApplicationTitle());
						LOG.debug(" --- DbConnectionString: " + cfg.getDbConnectionString());
						LOG.debug(" --- WorkspacePath: " + cfg.getWorkspacePath());
						LOG.debug(" --- isDebug: " + cfg.isDebug());
					} catch (JAXBException ex) {
						LOG.error(ex.toString());
						ex.printStackTrace();
					}
				} else 
					LOG.error("Entry \""+cfgDocPath+"\" not found!");
			} else
				LOG.error("cfgDocPath is null!");
		} else
			LOG.error("Reference on owner Bundle is null!");
		LOG.debug("doOnInit - ends.");
}

	public BioConfig getCurrentConfig() {
		return this.config;
	}
}
