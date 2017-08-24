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

import com.jimmyhowe.colorconsole.Console;
import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;

/**
 * SQLite Connector
 *
 * Connects to the JDBC using SQLite Driver
 */
public class SQLiteConnector extends Connector
{
    /**
     * @param plugin SQLite Plugin
     */
    public SQLiteConnector(Plugin plugin)
    {
        super(plugin);
    }

    /**
     * Connect to the DB
     *
     * @param config Config
     *
     * @return JDBC Connection
     */
    @Nullable
    public Connection connect(@NotNull Properties config)
    {
        String database = config.getProperty("database");

        if ( Objects.equals(database, ":memory:") )
        {
            return this.createConnection("jdbc:sqlite::memory:", config, config);
        }

        String realPath = getRealPath(database);

        if ( realPath == null )
        {
            throw new InvalidArgumentException("Database (" + database + ") does not exist.");

        }

        return this.createConnection("jdbc:sqlite:" + database, config, config);
    }

    /**
     * Returns the Real Path of the Database
     *
     * @param database Database Name
     *
     * @return Full Name
     */
    @Nullable
    protected String getRealPath(@NotNull String database)
    {
        try
        {
            File file = new File(database);

            if ( file.exists() )
            {
                return file.getCanonicalPath();
            } else
            {
                Console.cyan(file.getCanonicalPath() + " doesn't exist");
            }

        } catch ( IOException e )
        {
            e.printStackTrace();
        }

        return null;
    }
}
