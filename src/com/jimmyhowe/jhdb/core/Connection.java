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

import com.jimmyhowe.colorconsole.Console;
import com.jimmyhowe.jhdb.core.exceptions.ConnectionException;
import com.jimmyhowe.jhdb.core.processors.PostProcessor;
import com.jimmyhowe.jhdb.core.processors.TableProcessor;
import com.jimmyhowe.jhdb.core.queries.QueryBuilder;
import com.jimmyhowe.jhdb.core.queries.QueryGrammar;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import com.jimmyhowe.jhdb.core.support.ExceptionHandler;
import com.jimmyhowe.jhdb.core.support.RunCallback;
import com.jimmyhowe.jhdb.core.utilities.QueryLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC Connector
 */
public abstract class Connection
{
    /**
     * The query log instance.
     */
    private final QueryLog queryLog = new QueryLog();

    /**
     * The adapter used to make the connection.
     */
    @NotNull
    private Plugin plugin;

    /**
     * Connector
     */
    private Connector connector;

    /**
     * The com.jimmyhowe.jhdb.schema grammar implementation.
     */
    @Nullable
    private SchemaGrammar schemaGrammar;

    /**
     * The query grammar implementation.
     */
    @Nullable
    private QueryGrammar queryGrammar;

    /**
     * The JHDB connection.
     */
    private java.sql.Connection connection;

    /**
     * The result set of a run select query.
     */
    @Nullable
    private ResultSet cachedResults;

    /**
     * The status of a run query.
     */
    private boolean queryStatus;

    /**
     * The number of updated records after an insert, update or delete query.
     */
    private int updatedRecords;

    /**
     * The post processor instance
     */
    private PostProcessor postProcessor = new TableProcessor();

    /**
     * Table Prefix
     */
    @NotNull
    private String tablePrefix = "";

    /**
     * Don't hit the DB
     */
    private boolean pretend = false;

    /**
     * Exception Handler
     */
    @NotNull
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * Instantiates the connection requiring the Plugin
     *
     * @param plugin Connection Plugin
     */
    public Connection(@NotNull Plugin plugin)
    {
        Initialize(plugin);
    }

    /**
     * Initializes the Connection
     *
     * @param plugin Connection Plugin
     */
    private void Initialize(@NotNull Plugin plugin)
    {
        this.plugin = plugin;
        this.connector = plugin.getConnector();
        this.schemaGrammar = plugin.getSchemaGrammar();
        this.queryGrammar = plugin.getQueryGrammar();
    }

    /**
     * Makes a connection to the Database
     *
     * @param url
     */
    private void connect(@NotNull Plugin url)
    {
        DB.getRunningLog().info("Attempting connection... (" + plugin.getDriver() + ")");

        this.connection = this.connector.connect(plugin.getProperties());
    }

    /**
     * Runs a query against the connection
     *
     * @param query    SQL Query
     * @param callback Run Callback
     *
     * @return Connection query was run against
     */
    @NotNull
    public Connection run(@Nullable String query, @NotNull RunCallback callback)
    {
        Console.white("DB.run : " + query);

        if ( query == null )
        {
            throw new ConnectionException("Connection: NULL Query Provided.");
        }

        if ( connection == null )
        {
            connect(plugin);
        }

        long start = System.nanoTime();

        // Connection is null when no wamp

        if ( connection != null )
        {
            try
            {
                Console.blue(String.valueOf(connection.isClosed()));

                Console.yellow(connection.toString());
                Statement statement = getStatement(query);
                callback.run(this, statement);
            } catch ( SQLException e )
            {
                e.printStackTrace();
            }
        } else
        {
            DB.getDispatcher().dispatch("cant-connect");
        }

        queryLog.query(query, getElapsedTime(start));

        return this;
    }

    /**
     * @param query
     *
     * @return JDBC Statement
     *
     * @throws SQLException When statement cant run
     */
    protected Statement getStatement(String query) throws SQLException
    {
        return getConnection().createStatement();
    }

    /**
     * Executes a query against the connection.
     *
     * @param query SQL Query
     *
     * @return True or False depending on success
     */
    public boolean execute(@NotNull String query)
    {
        return run(query, (me, statement) -> queryStatus = ! statement.execute(query)).queryStatus();
    }

    /**
     * Runs an SQL statement against the connection
     *
     * @param query SQL Query
     *
     * @return Results
     */
    @Nullable
    public ResultSet statement(String query)
    {
        return run(query, (me, statement) ->
        {
            if ( me.pretend )
            {
                cachedResults = null;

                return;
            }

            cachedResults = statement.executeQuery(query);
        }).cachedResults();
    }

    /**
     * Performs an affecting statement
     *
     * @param query SQL Query
     *
     * @return Number of affected rows
     */
    public int affectingStatement(String query)
    {
        return run(query, (me, statement) -> updatedRecords = statement.executeUpdate(query)).updatedRecords();
    }

    /**
     * Performs a SELECT query.
     *
     * @param query SQL Query
     *
     * @return Results
     */
    @Nullable
    public ResultSet select(String query)
    {
        return statement(query);
    }

    /**
     * Perform a SELECT query with bindings.
     *
     * @param query    SQL query
     * @param bindings Bindings
     *
     * @return
     */
    @Nullable
    public ResultSet select(String query, Object... bindings)
    {
        return statement(queryWithBindings(query, bindings));
    }

    /**
     * Add bindings to query
     *
     * @param query    SQL query
     * @param bindings Bindings
     *
     * @return SQL
     */
    private String queryWithBindings(String query, Object[] bindings)
    {
        return null;
    }

    /**
     * Performs an INSERT statement.
     *
     * @param query SQL Query
     *
     * @return Number of affected rows
     */
    public int insert(String query)
    {
        return affectingStatement(query);
    }

    /**
     * Performs an UPDATE statement.
     *
     * @param query SQL Query
     *
     * @return Number of affected rows
     */
    public int update(String query)
    {
        return affectingStatement(query);
    }

    /**
     * Performs a DELETE statement.
     *
     * @param query SQL
     *
     * @return Number of affected rows
     */
    public int delete(String query)
    {
        return affectingStatement(query);
    }

    /**
     * Gets the adapter on the connection.
     *
     * @return The connections Adapter
     */
    @NotNull
    public Plugin getPlugin()
    {
        return plugin;
    }

    /**
     * Gets the JDBC Connection
     *
     * @return The JDBC Connection
     */
    public java.sql.Connection getConnection()
    {
        return connection;
    }

    /**
     * Closes the connection.
     */
    public void closeConnection()
    {
//        if ( connection != null )
//        {
//            try
//            {
//                connection.close();
//            } catch ( SQLException e )
//            {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * Returns the result set.
     *
     * @return Cached ResultSet
     */
    @Nullable
    private ResultSet cachedResults()
    {
        return cachedResults;
    }

    /**
     * Returns the query status.
     *
     * @return if the query is not result set
     *
     * @implNote Needs renaming
     */
    private boolean queryStatus()
    {
        return queryStatus;
    }

    /**
     * Returns the number of updated records.
     *
     * @return Number of Updated records
     */
    private int updatedRecords()
    {
        return updatedRecords;
    }

    /**
     * Returns the elapsed time
     *
     * @param start Start time
     *
     * @return Elapsed time in microseconds
     */
    private long getElapsedTime(long start)
    {
        long end = System.nanoTime();

        return (end - start) / 1000;
    }

    /**
     * Returns the query log for this connection.
     *
     * @return Query Log
     */
    @NotNull
    public QueryLog getQueryLog()
    {
        return queryLog;
    }

    /**
     * Returns the com.jimmyhowe.jhdb.schema grammar for this connection.
     *
     * @return Schema Grammar Instance
     */
    @Nullable
    public SchemaGrammar getSchemaGrammar()
    {
        return schemaGrammar;
    }

    /**
     * Returns the query grammar for this connection.
     *
     * @return Query Grammar Instance
     */
    @Nullable
    public QueryGrammar getQueryGrammar()
    {
        return queryGrammar;
    }

    /**
     * Returns a new query builder instance for this table.
     *
     * @param table Table name
     *
     * @return Query Builder Instance
     */
    @NotNull
    public QueryBuilder table(String table)
    {
        return new QueryBuilder(this, getQueryGrammar(), postProcessor).from(table);
    }

    /**
     * Begins a transaction on this connection.
     */
    public void beginTransaction()
    {
        try
        {
            this.getConnection().setAutoCommit(false);
        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Rolls back an transaction on this connection.
     */
    public void rollbackTransaction()
    {
        try
        {
            this.getConnection().rollback();
        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Commits a transaction stored on this connection.
     */
    public void commitTransaction()
    {
        try
        {
            this.getConnection().commit();
        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Post Processor on the connection.
     *
     * @param postProcessor Post Processor Instance
     */
    public void setPostProcessor(PostProcessor postProcessor)
    {
        this.postProcessor = postProcessor;
    }

    /**
     * @return The Table Prefix
     */
    @NotNull
    public String getTablePrefix()
    {
        return tablePrefix;
    }

    /**
     * Create a database on the connection
     *
     * @param database Database Name
     *
     * @return Successful
     */
    public boolean createDatabase(String database)
    {
        return execute("CREATE DATABASE " + database);
    }
}
