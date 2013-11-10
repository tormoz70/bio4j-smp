package bio4j.server.api.helpers;

import org.osgi.framework.Bundle;

import bio4j.server.api.helpers.BioHelper;

public interface BioHandlerMapper extends BioHelper {
	void doScanServicePackage(Class<?> serviceType, Bundle bundle, RegisterHandlerDelegate registerHandler);
}
