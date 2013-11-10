package bio4j.database.api;

public interface UserAuthService {
	boolean login(String name, String password);

	boolean register(String name, String password);
}
