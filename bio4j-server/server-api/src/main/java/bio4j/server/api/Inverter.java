package bio4j.server.api;

public interface Inverter
{
    String login(String name, String password);
    String register(String name, String password);
}
