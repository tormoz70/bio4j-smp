package bio4j.server.config;

import bio4j.server.api.services.BioConfigService;
import bio4j.server.common.config.BioConfigProps;
import bio4j.server.common.config.BioConfigServiceBase;

@BioConfigProps(path = "/res/bio4j.xml")
public class BioConfigServiceImpl extends BioConfigServiceBase implements BioConfigService {
}
