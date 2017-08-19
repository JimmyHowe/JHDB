package com.jimmyhowe.jhdb.mysql;

import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.Connector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;

/**
 * MySQL Connector
 *
 * Connects to a MySQL connection
 */
public class MySQLConnector extends Connector
{
    public MySQLConnector(Plugin plugin)
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
    @Override
    public Connection connect(@NotNull Properties config)
    {
        String dns = this.getDsn(config);

        Properties options = new Properties();

        Connection connection = createConnection(dns, config, options);

//        if(database == null)
//        {
//            // connection.exec("USE `" + database + "`;");
//        }

        return connection;
    }

    private String getDsn(@NotNull Properties config)
    {
        return this.getHostDsn(config);
    }

    private String getHostDsn(@NotNull Properties config)
    {
        String host = (String) config.get("host");
        String port = ! Objects.equals(config.get("port"), "") ? ":" + config.get("port") : "";
        String database = ! Objects.equals(config.get("database"), "") ? "/" + config.get("database") : "";

        // FIX SSL

        return String.format("jdbc:mysql://%s%s%s?useSSL=false", host, port, database);
    }
}
