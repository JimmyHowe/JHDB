package com.jimmyhowe.jhdb.core;

import com.jimmyhowe.jhdb.core.queries.QueryGrammar;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * Abstract Adapter
 */
public abstract class Adapter
{
    /**
     * @return Driver String
     */
    @NotNull
    public abstract String getDriver();

    /**
     * @return A fresh Connection
     */
    @NotNull
    public abstract Connector getConnector();

    /**
     * @return A fresh Connection
     */
    @NotNull
    public abstract Connection getConnection();

    /**
     * @return A fresh Connection
     */
    @Nullable
    public abstract QueryGrammar getQueryGrammar();

    /**
     * @return A fresh Connection
     */
    @Nullable
    public abstract SchemaGrammar getSchemaGrammar();

    /**
     * @return Properties Object
     */
    @Nullable
    public abstract Properties getProperties();
}
