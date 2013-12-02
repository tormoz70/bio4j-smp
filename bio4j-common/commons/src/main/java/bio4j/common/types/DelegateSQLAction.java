package bio4j.common.types;

import java.sql.SQLException;

public interface DelegateSQLAction<T> {
    public abstract T execute() throws SQLException;
}
