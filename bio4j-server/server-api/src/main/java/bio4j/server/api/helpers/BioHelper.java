package bio4j.server.api.helpers;

import org.osgi.framework.BundleContext;

import bio4j.server.api.BioEnvironment;

public interface BioHelper {
    void init(BioEnvironment environment);
    BioEnvironment getEnvironment();
    BundleContext getBundleContext();
    String getHelperName();
}
