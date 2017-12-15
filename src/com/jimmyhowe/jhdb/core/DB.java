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

import com.jimmyhowe.dispatcher.Dispatcher;
import com.jimmyhowe.dispatcher.Listener;
import com.jimmyhowe.jhdb.core.exceptions.InvalidArgumentException;
import com.jimmyhowe.jhdb.core.queries.QueryBuilder;
import com.jimmyhowe.jhlog.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Database Facade
 */
public class DB
{
    /**
     * Default Plugin Key
     */
    public static final String DEFAULT_PLUGIN_KEY = "_default";

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
    @NotNull
    public static Dispatcher dispatcher = new Dispatcher();

    /**
     * List of Plugins
     */
    @NotNull
    private static LinkedHashMap<String, Plugin> plugins = new LinkedHashMap<>();

    /**
     * List of Connections
     */
    @NotNull
    private static LinkedHashMap<String, Connection> connections = new LinkedHashMap<>();

    /**
     * Default Plugin Key
     */
    private static String defaultPluginKey;

    /**
     * DB Facade log
     */
    @NotNull
    private static Log runningLog = new Log();

    /**
     * This registers the default connection using the provided adapter and stores it in the list of connections.
     *
     * @param plugin Connection Adapter
     *
     * @return The Connection
     */
    public static Plugin use(@NotNull Plugin plugin)
    {
        return register(DEFAULT_PLUGIN_KEY, plugin);
    }

    /**
     * Alias for use, but allows for passing of plugin class.
     *
     * @param pluginClass Class of the plugin
     *
     * @return The connection associated with the plugin
     */
    public static Plugin use(@NotNull Class<? extends Plugin> pluginClass)
    {
        Plugin plugin = initializePlugin(pluginClass);

        assert plugin != null;

        return use(plugin);
    }

    /**
     * @return The default plugin
     */
    public static Plugin getDefaultPlugin()
    {
        return plugins.get(defaultPluginKey);
    }

    /**
     * This function registers the plugin inside the container and stores for future use.
     *
     * @param key    Plugin Key
     * @param plugin Plugin Adapter
     *
     * @return The stored plugin
     */
    public static Plugin register(String key, @NotNull Plugin plugin)
    {
        if ( plugins.isEmpty() )
        {
            defaultPluginKey = key;
        }

        plugins.put(key, plugin);

        DB.getRunningLog().info("Registered " + plugin.getClass().getSimpleName() + " plugin as '" + key + "'.");

        return plugins.get(key);
    }

    /**
     * This function registers the plugin inside the container and stores for future use.
     *
     * Cleaner usage.
     *
     * @param key         Plugin Key
     * @param pluginClass Class of plugin for instantiation
     */
    public static Plugin register(String key, @NotNull Class<? extends Plugin> pluginClass)
    {
        Plugin plugin = initializePlugin(pluginClass);

        if ( plugins.isEmpty() )
        {
            defaultPluginKey = key;
        }

        plugins.put(key, plugin);

        DB.getRunningLog().info("Registered " + plugin.getClass().getSimpleName() + " plugin as '" + key + "'.");

        return plugin;
    }

    /**
     * @param pluginClass Plugin Class
     *
     * @return New instance of plugin or null if error occurred
     */
    private static Plugin initializePlugin(@NotNull Class<? extends Plugin> pluginClass)
    {
        try
        {
            return pluginClass.newInstance();
        } catch ( @NotNull InstantiationException | IllegalAccessException e )
        {
            e.printStackTrace();
        }

        throw new InvalidArgumentException("Plugin cannot be instantiated.");
    }

    /**
     * @return The registered plugins
     */
    public static @NotNull LinkedHashMap<String, Plugin> getRegisteredPlugins()
    {
        return plugins;
    }

    /**
     * @param key Connection Key
     *
     * @return Connection
     */
    public static Connection connection(String key)
    {
        if ( ! hasPlugin(key) )
        {
            throw new InvalidArgumentException("Plugin '" + key + "' does not exist.");
        }

        if ( ! connections.containsKey(key) )
        {
            throw new InvalidArgumentException("Connection '" + key + "' does not exist.");
        }

        return connections.get(key);
    }

    /**
     * @param key Plugin key
     *
     * @return true or false if plugin exists
     */
    private static boolean hasPlugin(String key)
    {
        return plugins.containsKey(key);
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
        if ( ! plugins.containsKey(defaultPluginKey) )
        {
            throw new InvalidArgumentException("Default plugin is not set.");
        }

        return plugins.get(defaultPluginKey).getConnection();
    }

    /**
     * Flushes all the connections by iterating through the list of connections and closing them.
     *
     * TODO: flush debug message cant be registered if there is no connection. TODO: Consider using only one log object
     * on the DB facade, and tagging log messages with connection details.
     */
    public static void flushConnections()
    {
        DB.getRunningLog().debug("Flushing connections.");

        DB.getDispatcher().dispatch("flushing");

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
    public static String getDefaultPluginKey()
    {
        return defaultPluginKey;
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
    //     */

    //    public static QueryLog getQueryLog()

    //    {

    //     * @return The default connections query log.

    //        return getDefaultConnection().getQueryLog();
//    /**

//    }

    /**
     * Outputs queries to the stdout before processing
     */
    public static void liveQueries()
    {
        liveQueries = true;
    }

    /**
     * @param query Query string
     *
     * @return ResultSet
     */
    public static @Nullable ResultSet statement(String query)
    {
        return getDefaultConnection().statement(query);
    }

    /**
     * @param query Query string
     *
     * @return True or false on success
     */
    public static boolean execute(@NotNull String query)
    {
        return getDefaultConnection().execute(query);
    }

    /**
     * @return The Dispatcher Instance
     */
    @NotNull
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

    /**
     * Fire event when the JDBC Connection is created.
     *
     * @param listener Callback
     */
    public static void onJdbcConnectionCreated(Listener listener)
    {
        dispatcher.listen("connector.created", listener);
    }

    /**
     * @return DB Log
     */
    @NotNull
    public static Log getRunningLog()
    {
        return runningLog;
    }

    /**
     * @param name Plugin name
     *
     * @return
     */
    public static Plugin getPlugin(String name)
    {
        return plugins.get(name);
    }

    /**
     * @param plugin Plugin name
     *
     * @return Connection
     */
    public static Connection resolveConnectionFromPlugin(String plugin)
    {
        return getPlugin(plugin).getConnection();
    }

    /**
     * Empties the plugins
     */
    public static void flushPlugins()
    {
        plugins = new LinkedHashMap<>();
    }

    /**
     * @return List of connections
     */
    public static Set<String> getConnectionList()
    {
        return connections.keySet();
    }
}
