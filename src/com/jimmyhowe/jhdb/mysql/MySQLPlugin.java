/*
 * MIT License
 *
 * Copyright (c) 2017 Jimmy Howe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jimmyhowe.jhdb.mysql;

import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class MySQLPlugin implements Plugin
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

    @NotNull
    public String getDriver()
    {
        return driver;
    }

    @NotNull
    public MySQLPlugin setDriver(String driver)
    {
        this.driver = driver;

        return this;
    }

    public String getHost()
    {
        return host;
    }

    @NotNull
    public MySQLPlugin setHost(String host)
    {
        this.host = host;

        return this;
    }

    public String getPort()
    {
        return port;
    }

    @NotNull
    public MySQLPlugin setPort(String port)
    {
        this.port = port;

        return this;
    }

    public String getDatabase()
    {
        return database;
    }

    @NotNull
    public MySQLPlugin setDatabase(String database)
    {
        this.database = database;

        return this;
    }

    public String getUsername()
    {
        return username;
    }

    @NotNull
    public MySQLPlugin setUsername(String username)
    {
        this.username = username;

        return this;
    }

    public String getPassword()
    {
        return password;
    }

    @NotNull
    public MySQLPlugin setPassword(String password)
    {
        this.password = password;

        return this;
    }

    public String getCharset()
    {
        return charset;
    }

    @NotNull
    public MySQLPlugin setCharset(String charset)
    {
        this.charset = charset;

        return this;
    }

    public String getCollation()
    {
        return collation;
    }

    @NotNull
    public MySQLPlugin setCollation(String collation)
    {
        this.collation = collation;

        return this;
    }

    public String getPrefix()
    {
        return prefix;
    }

    @NotNull
    public MySQLPlugin setPrefix(String prefix)
    {
        this.prefix = prefix;

        return this;
    }

    public boolean isStrict()
    {
        return strict;
    }

    @NotNull
    public MySQLPlugin setStrict(boolean strict)
    {
        this.strict = strict;

        return this;
    }

    public String getEngine()
    {
        return engine;
    }

    @NotNull
    public MySQLPlugin setEngine(String engine)
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
