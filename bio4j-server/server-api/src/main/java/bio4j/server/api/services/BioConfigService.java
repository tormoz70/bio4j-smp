package bio4j.server.api.services;

import bio4j.server.model.BioConfig;

public interface BioConfigService extends BioService {
	BioConfig getCurrentConfig();
}
