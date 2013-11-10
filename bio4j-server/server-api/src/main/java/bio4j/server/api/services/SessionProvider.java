package bio4j.server.api.services;

import javax.servlet.http.HttpSession;

public interface SessionProvider extends BioService {
    void register(String sessionId, HttpSession session);
}
