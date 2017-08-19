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
    private Adapter driver;

    public Connector(Adapter driver)
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
