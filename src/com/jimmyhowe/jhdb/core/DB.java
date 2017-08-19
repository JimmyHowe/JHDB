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

import com.jimmyhowe.jhdb.core.exceptions.InvalidArgumentException;
import com.jimmyhowe.jhdb.core.queries.QueryBuilder;
import com.jimmyhowe.jhdb.core.utilities.QueryLog;
import com.jimmyhowe.jhdb.events.Dispatcher;
import com.jimmyhowe.jhdb.events.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Database Facade
 */
public class DB
{
    /**
     * Outputs queries before performing
     */
    public static boolean liveQueries = false;

    /**
     * Prints stack trace when connection error occurs
     */
    public static boolean printStackTrace = false;

    /**
     * Event Dispatcher
     */
    public static Dispatcher dispatcher = new Dispatcher();

    /**
     * List of Connections
     */
    @NotNull
    private static LinkedHashMap<String, Connection> connections = new LinkedHashMap<>();

    /**
     * Default Connection Key
     */
    private static String defaultConnectionKey;

    /**
     * This registers the default connection using the provided adapter and stores it in the list of connections.
     *
     * @param plugin Connection Adapter
     *
     * @return The Connection
     */
    public static Connection use(@NotNull Plugin plugin)
    {
        return register("default", plugin);
    }

    /**
     * This stores the connection from the adapter in the list of adapters but provides a lookup key. if this is the
     * first connection then it stores the key as the default connection key.
     *
     * @param key     Connection Key
     * @param plugin Connection Adapter
     *
     * @return The Connection
     */
    public static Connection register(String key, @NotNull Plugin plugin)
    {
        if ( connections.isEmpty() )
        {
            defaultConnectionKey = key;
        }

        connections.put(key, plugin.getConnection());

        return connections.get(key);
    }

    /**
     * @param key Connection Key
     *
     * @return Connection
     */
    public static Connection connection(String key)
    {
        if ( ! connections.containsKey(key) )
        {
            throw new RuntimeException("DB: No connections set");
        }

        return connections.get(key);
    }

    /**
     * Gets the default connection.
     *
     * @return Default Connection
     *
     * @throws RuntimeException When no default connection has been set
     */
    public static Connection getDefaultConnection()
    {
        if ( ! connections.containsKey(defaultConnectionKey) )
        {
            throw new InvalidArgumentException("DB: No default connection set");
        }

        return connections.get(defaultConnectionKey);
    }

    /**
     * Flushes all the connections by iterating through the list of connections and closing them.
     */
    public static void flushConnections()
    {
        for ( Map.Entry<String, Connection> entries : connections.entrySet() )
        {
            entries.getValue().closeConnection();
        }

        connections = new LinkedHashMap<>();

        DB.getDispatcher().dispatch("flushed");
    }

    /**
     * Returns a QueryBuilder instance using the default connection.
     *
     * @param table Table name
     *
     * @return Query Builder
     */
    public static QueryBuilder table(String table)
    {
        return getDefaultConnection().table(table);
    }

    /**
     * Returns a QueryBuilder instance using the provided connection key.
     *
     * @param table Table name
     *
     * @return Query Builder
     */
    public static QueryBuilder table(String table, String connection)
    {
        return connection(connection).table(table);
    }

    /**
     * Returns the key of the default connection.
     *
     * @return Default Connection Key
     */
    public static String getDefaultConnectionKey()
    {
        return defaultConnectionKey;
    }

    /**
     * Performs a select query against the default connection.
     *
     * @param query SQL
     *
     * @return Results
     */
    @Nullable
    public static ResultSet select(String query)
    {
        return getDefaultConnection().select(query);
    }

    /**
     * Performs an insert on the default connection.
     *
     * @param query SQL
     *
     * @return Number of affected rows
     */
    public static int insert(String query)
    {
        return getDefaultConnection().insert(query);
    }

    /**
     * Performs an update on the default connection.
     *
     * @param query SQL
     *
     * @return Number of affected rows
     */
    public static int update(String query)
    {
        return getDefaultConnection().update(query);
    }

    /**
     * Performs a DELETE statement.
     *
     * @param query SQL
     *
     * @return Number of affected rows
     */
    public static int delete(String query)
    {
        return getDefaultConnection().delete(query);
    }

    /**
     * Begins a transaction on the default connection.
     */
    public static void beginTransaction()
    {
        getDefaultConnection().beginTransaction();
    }

    /**
     * Rolls back a transaction on the default connection.
     */
    public static void rollbackTransaction()
    {
        getDefaultConnection().rollbackTransaction();
    }

    /**
     * Commits a transaction on the default connection.
     */
    public static void commitTransaction()
    {
        getDefaultConnection().commitTransaction();
    }

    /**
     * @return The default connections query log.
     */
    public static QueryLog getQueryLog()
    {
        return getDefaultConnection().getQueryLog();
    }

    /**
     * Outputs com.jimmyhowe.jhdb.queries to the stdout before processing
     */
    public static void liveQueries()
    {
        liveQueries = true;
    }

    public static @Nullable ResultSet statement(String query)
    {
        return getDefaultConnection().statement(query);
    }

    public static boolean execute(String query)
    {
        return getDefaultConnection().execute(query);
    }

    /**
     * @return The Dispatcher Instance
     */
    public static Dispatcher getDispatcher()
    {
        return dispatcher;
    }

    /**
     * Cant Connect to DB
     *
     * @param listener Callback
     */
    public static void onCantConnect(Listener listener)
    {
        dispatcher.listen("cant-connect", listener);
    }

    /**
     * When the Connections are flushed
     *
     * @param listener Callback
     */
    public static void onFlushed(Listener listener)
    {
        dispatcher.listen("flushed", listener);
    }

    public static void onJdbcConnectionCreated(Listener listener)
    {
        dispatcher.listen("connector.created", listener);
    }
}
