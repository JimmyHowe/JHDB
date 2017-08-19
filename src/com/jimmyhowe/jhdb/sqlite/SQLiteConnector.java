package com.jimmyhowe.jhdb.sqlite;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.Connector;
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
            } else {
                Console.cyan(file.getCanonicalPath() + " doesn't exist");
            }

        } catch ( IOException e )
        {
            e.printStackTrace();
        }

        return null;
    }
}
