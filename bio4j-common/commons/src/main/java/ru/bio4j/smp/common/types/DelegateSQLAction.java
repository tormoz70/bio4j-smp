package ru.bio4j.smp.common.types;

import java.sql.SQLException;

public interface DelegateSQLAction<T> {
    public abstract T execute() throws SQLException;
}
