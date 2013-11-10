package bio4j.server.api.services;

import org.osgi.framework.BundleContext;

import bio4j.server.api.BioEnvironment;
import bio4j.server.api.Delegate;


public interface BioService {
    void init(BioEnvironment environment);
    BioEnvironment getEnvironment();
    BundleContext getBundleContext();
    String getServiceName();
}
