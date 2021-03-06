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

package com.jimmyhowe.jhdb.core;

import com.jimmyhowe.jhdb.core.support.DetectsLostConnections;
import com.jimmyhowe.jhdb.core.support.DetectsMissingDrivers;
import com.jimmyhowe.jhdb.core.support.ReloadsDrivers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connector
 */
public abstract class Connector implements DetectsLostConnections, DetectsMissingDrivers, ReloadsDrivers
{
    private Plugin driver;

    public Connector(Plugin driver)
    {
        this.driver = driver;
    }

    /**
     * Connect to the DB
     *
     * The abstract connect method is used to connect to the database.
     *
     * @param config Config Options
     *
     * @return JDBC Connection
     */
    @Nullable
    public abstract Connection connect(Properties config);

    /**
     * Create a connection to the DB
     *
     * @param dsn     Data Source Name
     * @param config  Config
     * @param options Options
     *
     * @return JDBC Connection
     */
    @Nullable
    public Connection createConnection(@NotNull String dsn, Properties config, Properties options)
    {
        // TODO: Move these

        String username = "root";
        String password = "";

        try
        {
            return this.createJdbcConnection(dsn, username, password, options);
        } catch ( SQLException firstException )
        {
            try
            {
                return this.tryAgainIfCausedByLostConnection(firstException, dsn, username, password, options);
            } catch ( SQLException secondException )
            {
                try
                {
                    return this.tryAgainIfCausedByMissingDriver(firstException, dsn, username, password, options);
                } catch ( SQLException thirdException )
                {
                    return null;
                }
            }
        }
    }

    /**
     * @param dsn      Data Name Source
     * @param username Username
     * @param password Password
     * @param options  Options
     *
     * @return JDBC Connection
     *
     * @throws SQLException When can't connect to DB
     */
    protected Connection createJdbcConnection(@NotNull String dsn, String username, String password, Properties options) throws SQLException
    {
        DB.getDispatcher().dispatch("connector.creating", dsn);

        return DriverManager.getConnection(dsn, username, password);
    }

    /**
     * @param e        SQLException
     * @param dsn      Data Name Source
     * @param username Username
     * @param password Password
     * @param options  Options
     *
     * @return JDBC Connection if successful, null if not
     */
    @Nullable
    protected Connection tryAgainIfCausedByLostConnection(@NotNull SQLException e, @NotNull String dsn, String username, String password, Properties options) throws SQLException
    {
        if ( causedByLostConnections(e) )
        {
            System.out.println("!! Caused by Lost Connection !!");
        }

        return createJdbcConnection(dsn, username, password, options);
    }


    @Nullable
    private Connection tryAgainIfCausedByMissingDriver(@NotNull SQLException e, @NotNull String dsn, String username, String password, Properties options) throws SQLException
    {
        if ( causedByMissingDriver(e) )
        {
            reloadDriver(this.driver);
        }

        return createJdbcConnection(dsn, username, password, options);
    }
}
