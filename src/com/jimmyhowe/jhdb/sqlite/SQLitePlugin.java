package com.jimmyhowe.jhdb.sqlite;

import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.queries.QueryGrammar;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * SQLite Plugin
 */
public class SQLitePlugin implements Plugin
{
    private String driver = "org.sqlite.JDBC";

    private String database = "database.sqlite";

    private String prefix = "";

    /**
     * @return Driver String
     */
    @Override
    public @NotNull String getDriver()
    {
        return "org.sqlite.JDBC";
    }

    /**
     * @return Connector Instance
     */
    @Override
    public @NotNull Connector getConnector()
    {
        return new SQLiteConnector(this);
    }

    /**
     * @return Connection Instance
     */
    @Override
    public @NotNull Connection getConnection()
    {
        return new SQLiteConnection(this);
    }

    /**
     * @return Query Grammar Instance
     */
    @Override
    public @Nullable QueryGrammar getQueryGrammar()
    {
        return new SQLiteQueryGrammar();
    }

    /**
     * @return Schema Grammar Instance
     */
    @Override
    public @Nullable SchemaGrammar getSchemaGrammar()
    {
        return new SQLiteSchemaGrammar();
    }

    /**
     * @return Properties Object
     */
    @Override
    public @Nullable Properties getProperties()
    {
        Properties properties = new Properties();

        properties.setProperty("driver", driver);
        properties.setProperty("database", database);
        properties.setProperty("prefix", prefix);

        return properties;
    }

    /**
     * @param driver Driver String
     */
    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    /**
     * @return Database name
     */
    public String getDatabase()
    {
        return database;
    }

    /**
     * @param database Database name
     */
    public void setDatabase(String database)
    {
        this.database = database;
    }

    /**
     * @return Table prefix
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * @param prefix Table prefix
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}