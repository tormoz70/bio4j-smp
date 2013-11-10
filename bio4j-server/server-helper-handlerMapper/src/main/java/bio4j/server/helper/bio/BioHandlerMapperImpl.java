package bio4j.server.helper.bio;

import java.util.List;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio4j.common.utils.Utl;
import bio4j.server.api.handlers.BioHandler;
import bio4j.server.common.BioHelperBase;
import bio4j.server.common.handler.RequestHandler;
import bio4j.server.api.helpers.BioHandlerMapper;
import bio4j.server.api.helpers.RegisterHandlerDelegate;

public class BioHandlerMapperImpl extends BioHelperBase implements BioHandlerMapper {

	private static RequestHandler classIsHandler(Class<?> clazz) {
		return Utl.findAnnotation(RequestHandler.class, clazz);
	}
	
	public void doScanServicePackage(Class<?> serviceType, Bundle bundle, RegisterHandlerDelegate registerHandler) {
		if(registerHandler == null) return;
		Class<?> clazz = serviceType;
		Logger log = LoggerFactory.getLogger(clazz);
		log.debug("starts...");
		if(bundle != null) {
			String pkgName = clazz.getPackage().getName();
			log.debug("Register handlers of package {" + pkgName + "}: ");
			
			List<Class<?>> clazzes = Utl.findClassesOfBandle(this.getBundleContext());
			log.debug("Found types: " + clazzes.size());
			for(Class<?> c : clazzes){
				RequestHandler rh = classIsHandler(c); 
				if((rh != null) && (BioHandler.class.isAssignableFrom(c))) {
					registerHandler.action(rh.mapping(), c);
					log.debug(" --- REGISTRED. Type [" + c + "] registred as handler? and mapped on \"" + rh.mapping() + "\"");
				} else 
					log.debug(" --- SKIPED. type [" + c + "] is not handler!");
			}
		} else
			log.error("Reference on owner Bundle is null!");
		log.debug("ends.");
	}
}
