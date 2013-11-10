package bio4j.server.service.security;

import bio4j.server.api.services.UserAuthService;
import bio4j.server.common.BioServiceBase;
import bio4j.server.model.User;

public class UserAuthServiceImpl extends BioServiceBase implements UserAuthService {
    public User login(String namepwdPair) {
    	return new User();
    }

}
