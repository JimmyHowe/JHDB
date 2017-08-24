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

package com.jimmyhowe.jhdb.sqlite;

import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.Plugin;
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
     * Constructs with no parameters
     */
    public SQLitePlugin()
    {

    }

    /**
     * Constructs with database name
     *
     * @param database Database name
     */
    public SQLitePlugin(String database)
    {
        this.database = database;
    }

    /**
     * @return Driver String
     */
    @Override
    public @NotNull String getDriver()
    {
        return "org.sqlite.JDBC";
    }

    /**
     * @param driver Driver string
     *
     * @return self
     */
    public SQLitePlugin setDriver(String driver)
    {
        this.driver = driver;

        return this;
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
     * @return Database name
     */
    public String getDatabase()
    {
        return database;
    }

    /**
     * @param database Database path
     *
     * @return self
     */
    public SQLitePlugin setDatabase(String database)
    {
        this.database = database;

        return this;
    }

    /**
     * @return Prefix string
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * @param prefix Prefix string
     *
     * @return self
     */
    public SQLitePlugin setPrefix(String prefix)
    {
        this.prefix = prefix;

        return this;
    }

    /**
     * Use in memory database
     *
     * @return self
     */
    public SQLitePlugin inMemory()
    {
        this.database = ":memory:";

        return this;
    }
}