package ru.bio4j.smp.common.types;

import java.sql.SQLException;

public interface DelegateSQLAction {
    public void execute() throws SQLException;
}
