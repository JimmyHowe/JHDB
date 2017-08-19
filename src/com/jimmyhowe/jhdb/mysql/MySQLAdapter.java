package com.jimmyhowe.jhdb.mysql;

import com.jimmyhowe.jhdb.core.Adapter;
import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.Connector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class MySQLAdapter extends Adapter
{
    private String driver = "com.mysql.jdbc.Driver";

    private String host = "localhost";

    private String port = "3306";

    private String database = "jhdb";

    private String username = "root";

    private String password = "";

    private String charset = "utf8";

    private String collation = "utf8_unicode_ci";

    private String prefix = "";

    private boolean strict = true;

    private String engine = "";

    public String getDriver()
    {
        return driver;
    }

    @NotNull
    public MySQLAdapter setDriver(String driver)
    {
        this.driver = driver;

        return this;
    }

    public String getHost()
    {
        return host;
    }

    @NotNull
    public MySQLAdapter setHost(String host)
    {
        this.host = host;

        return this;
    }

    public String getPort()
    {
        return port;
    }

    @NotNull
    public MySQLAdapter setPort(String port)
    {
        this.port = port;

        return this;
    }

    public String getDatabase()
    {
        return database;
    }

    @NotNull
    public MySQLAdapter setDatabase(String database)
    {
        this.database = database;

        return this;
    }

    public String getUsername()
    {
        return username;
    }

    @NotNull
    public MySQLAdapter setUsername(String username)
    {
        this.username = username;

        return this;
    }

    public String getPassword()
    {
        return password;
    }

    @NotNull
    public MySQLAdapter setPassword(String password)
    {
        this.password = password;

        return this;
    }

    public String getCharset()
    {
        return charset;
    }

    @NotNull
    public MySQLAdapter setCharset(String charset)
    {
        this.charset = charset;

        return this;
    }

    public String getCollation()
    {
        return collation;
    }

    @NotNull
    public MySQLAdapter setCollation(String collation)
    {
        this.collation = collation;

        return this;
    }

    public String getPrefix()
    {
        return prefix;
    }

    @NotNull
    public MySQLAdapter setPrefix(String prefix)
    {
        this.prefix = prefix;

        return this;
    }

    public boolean isStrict()
    {
        return strict;
    }

    @NotNull
    public MySQLAdapter setStrict(boolean strict)
    {
        this.strict = strict;

        return this;
    }

    public String getEngine()
    {
        return engine;
    }

    @NotNull
    public MySQLAdapter setEngine(String engine)
    {
        this.engine = engine;

        return this;
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public Connector getConnector()
    {
        return new MySQLConnector(this);
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public Connection getConnection()
    {
        return new MySQLConnection(this);
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public MySQLQueryGrammar getQueryGrammar()
    {
        return new MySQLQueryGrammar();
    }

    /**
     * @return A fresh Connection
     */
    @NotNull
    @Override
    public MySQLSchemaGrammar getSchemaGrammar()
    {
        return new MySQLSchemaGrammar();
    }

    /**
     * @return Properties Object
     */
    @Nullable
    @Override
    public Properties getProperties()
    {
        Properties properties = new Properties();

        properties.setProperty("driver", driver);
        properties.setProperty("host", host);
        properties.setProperty("port", port);
        properties.setProperty("database", database);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        properties.setProperty("charset", charset);
        properties.setProperty("collation", collation);
        properties.setProperty("prefix", prefix);
        properties.setProperty("strict", String.valueOf(strict));
        properties.setProperty("engine", engine);

        return properties;
    }
}
