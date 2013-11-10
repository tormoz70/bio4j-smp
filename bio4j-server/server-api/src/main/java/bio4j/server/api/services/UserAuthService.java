package bio4j.server.api.services;

import bio4j.server.model.User;

public interface UserAuthService extends BioService {
	User login(String namepwdPair);
}
