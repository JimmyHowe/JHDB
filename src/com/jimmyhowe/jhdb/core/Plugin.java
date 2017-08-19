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
     * @return A fresh Connection
     */
    @NotNull Connector getConnector();

    /**
     * @return A fresh Connection
     */
    @NotNull Connection getConnection();

    /**
     * @return A fresh Connection
     */
    @Nullable QueryGrammar getQueryGrammar();

    /**
     * @return A fresh Connection
     */
    @Nullable SchemaGrammar getSchemaGrammar();

    /**
     * @return Properties Object
     */
    @Nullable Properties getProperties();
}
