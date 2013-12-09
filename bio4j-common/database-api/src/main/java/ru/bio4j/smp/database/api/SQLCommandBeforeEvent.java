package ru.bio4j.smp.database.api;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 27.11.13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
public interface SQLCommandBeforeEvent {
    void handle(SQLCommand sender, SQLCommandBeforeEventAttrs attrs);
}
