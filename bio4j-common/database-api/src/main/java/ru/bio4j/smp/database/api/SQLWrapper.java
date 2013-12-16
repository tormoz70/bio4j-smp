package ru.bio4j.smp.database.api;

import java.sql.SQLException;

/**
 * Базовый враппер для SQL
 */
public interface SQLWrapper {
    public String prepare(String sql);
}
