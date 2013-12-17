package ru.bio4j.smp.database.api;

import ru.bio4j.smp.common.types.Params;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public interface SQLCommand {

	Params getParams();

    boolean cancel();

	Connection getConnection();

	Statement getStatement();

    Exception getLastError();

    void addBeforeEvent(SQLCommandBeforeEvent e);
    void addAfterEvent(SQLCommandAfterEvent e);
    void clearBeforeEvents();
    void clearAfterEvents();

    void setSqlWrapper(SQLWrapper sqlWrapper);

    String getPreparedSQL();

}
