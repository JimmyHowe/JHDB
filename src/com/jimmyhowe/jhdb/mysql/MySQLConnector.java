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

import com.jimmyhowe.jhdb.core.Connector;
import com.jimmyhowe.jhdb.core.Plugin;
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
