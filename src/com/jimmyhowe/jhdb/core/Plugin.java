package com.jimmyhowe.jhdb.core;

import com.jimmyhowe.jhdb.core.queries.QueryGrammar;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * Plugin Interface
 */
public interface Plugin
{
    /**
     * @return Driver String
     */
    @NotNull String getDriver();

    /**
     * @return Connector Instance
     */
    @NotNull Connector getConnector();

    /**
     * @return Connection Instance
     */
    @NotNull Connection getConnection();

    /**
     * @return Query Grammar Instance
     */
    @Nullable QueryGrammar getQueryGrammar();

    /**
     * @return Schema Grammar Instance
     */
    @Nullable SchemaGrammar getSchemaGrammar();

    /**
     * @return Properties Object
     */
    @Nullable Properties getProperties();
}
