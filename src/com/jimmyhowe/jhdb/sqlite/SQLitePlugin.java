package com.jimmyhowe.jhdb.sqlite;

import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.queries.QueryGrammar;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

/**
 * Created by Jimmy on 29/05/2017.
 */
public class SQLitePlugin implements Plugin
{
    private String driver = "com.";

    private String database = "database.sqlite";

    private String prefix = "";

    public String driver()
    {
        return driver;
    }

    @NotNull
    public SQLitePlugin driver(String driver)
    {
        this.driver = driver;

        return this;
    }

    public String database()
    {
        return database;
    }

    @NotNull
    public SQLitePlugin database(String database)
    {
        this.database = database;

        return this;
    }

    public String prefix()
    {
        return prefix;
    }

    @NotNull
    public SQLitePlugin prefix(String prefix)
    {
        this.prefix = prefix;

        return this;
    }

    /**
     * @return Driver String
     */
    @NotNull
    @Override
    public String getDriver()
    {
        return "org.sqlite.JDBC";
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public Connector getConnector()
    {
        return new SQLiteConnector(this);
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public Connection getConnection()
    {
        return new SQLiteConnection(this);
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public QueryGrammar getQueryGrammar()
    {
        return new SQLiteQueryGrammar();
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public SchemaGrammar getSchemaGrammar()
    {
        return new SQLiteSchemaGrammar();
    }

    /**
     * @return Properties Object
     */
    @NotNull
    @Override
    public Properties getProperties()
    {
        Properties properties = new Properties();

        properties.setProperty("driver", driver);
        properties.setProperty("database", database);
        properties.setProperty("prefix", prefix);

        return properties;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "SQLiteAdapter{" +
                "driver='" + driver + '\'' +
                ", database='" + database + '\'' +
                ", prefix='" + prefix + '\'' +
                "} " + super.toString();
    }
}
