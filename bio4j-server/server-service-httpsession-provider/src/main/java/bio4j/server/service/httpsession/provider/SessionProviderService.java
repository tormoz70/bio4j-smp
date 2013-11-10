package bio4j.server.service.httpsession.provider;

import javax.servlet.http.HttpSession;

import bio4j.server.common.BioServiceBase;
import bio4j.server.api.services.SessionProvider;

public class SessionProviderService extends BioServiceBase implements SessionProvider {

	@Override
	public void register(String sessionId, HttpSession session) {
		// TODO Auto-generated method stub
	}
}
