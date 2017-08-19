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

package com.jimmyhowe.jhdb.core.schema;

import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.DB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema Facade
 *
 * The com.jimmyhowe.jhdb.schema facade provides an interface to access underlying complex logic from the Schema
 * Builder.
 *
 * @author Jimmy Howe
 */
public class Schema
{
    /**
     * Creates a table in the database
     *
     * @param table    Table Name
     * @param callback TableBlueprint Callback
     */
    public static void create(String table, @NotNull Blueprint callback)
    {
        getSchemaBuilder().create(table, callback);
    }

    /**
     * @param key Connection Key
     *
     * @return Schema Builder
     */
    @NotNull
    public static SchemaBuilder connection(String key)
    {
        return new SchemaBuilder(DB.connection(key));
    }

    /**
     * @param tableName
     *
     * @return
     */
    @Contract(pure = true)
    public static boolean hasTable(String tableName)
    {
//        return getSchemaBuilder().hasTable(tableName);

        return false;
    }

    /**
     * @param tableName
     * @param columnName
     *
     * @return
     */
    @Contract(pure = true)
    public static boolean hasColumn(String tableName, String columnName)
    {
        return false;
    }

    /**
     * @param from
     * @param to
     *
     * @return
     */
    @Contract(pure = true)
    public static boolean rename(String from, String to)
    {
        return false;
    }

    /**
     * @param tableName
     *
     * @return
     */
    @Contract(pure = true)
    public static boolean dropIfExists(String tableName)
    {
        return false;
    }

    /**
     * @param table Table name
     */
    public static void drop(String table)
    {
        getSchemaBuilder().drop(table);
    }

    /**
     * Returns the Schema Builder for the default Connection
     *
     * @return The Default Connection's Schema Builder
     */
    @NotNull
    private static SchemaBuilder getSchemaBuilder()
    {
        return new SchemaBuilder(DB.getDefaultConnection());
    }

    /**
     * Allows for multiple connections to be used on the com.jimmyhowe.jhdb.schema builder as once. We cicle through the
     * connections
     *
     * @param keys Connection Keys
     *
     * @return Schema Builder
     */
    @NotNull
    public static SchemaBuilder connections(@NotNull String... keys)
    {
        List<Connection> connections = new ArrayList<>();

        // For each of the keys, extract the connection
        for ( String key : keys )
        {
            connections.add(DB.connection(key));
        }

        // Return aht com.jimmyhowe.jhdb.schema builder with multiple connections
        return new SchemaBuilder(connections);
    }
}
