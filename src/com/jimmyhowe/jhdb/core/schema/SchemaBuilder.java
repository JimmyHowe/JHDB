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
import com.jimmyhowe.jhdb.core.processors.TableProcessor;
import com.jimmyhowe.jhdb.core.tables.rows.Row;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema Builder
 */
public class SchemaBuilder
{
    /**
     * Default String Length
     */
    public static int defaultStringLength = 255;

    /**
     * DB Connection
     */
    private List<Connection> connections = new ArrayList<>();

    /**
     * The Table Blueprint
     */
    private Table blueprint;

    /**
     * @param connection Connection
     */
    public SchemaBuilder(Connection connection)
    {
        this.connections.add(connection);
    }

    /**
     * @param connections List of connectors
     */
    public SchemaBuilder(List<Connection> connections)
    {
        this.connections = connections;
    }

    /**
     * @param table Table name
     *
     * @return New TableBlueprint
     */
    private static Table createBlueprint(String table)
    {
        return new Table(table);
    }

    /**
     * @param blueprint Table Blueprint
     */
    private void build(@NotNull Table blueprint, @NotNull Connection connection)
    {
        blueprint.build(connection, connection.getSchemaGrammar());
    }

    /**
     * @param table    Table name
     * @param callback Table blueprint
     */
    public void create(String table, @NotNull Blueprint callback)
    {
        for ( Connection connection : connections )
        {
            performCreate(connection, table, callback);
        }
    }

    /**
     * @param connection Connection instance
     * @param table      Table name
     * @param callback   Blueprint callback
     */
    private void performCreate(@NotNull Connection connection, String table, @NotNull Blueprint callback)
    {
        blueprint = createBlueprint(table);

        blueprint.create();

        callback.build(blueprint);

        build(blueprint, connection);
    }

    /**
     * @param table Table name
     */
    public void drop(String table)
    {
        for ( Connection connection : connections )
        {
            performDrop(connection, table);
        }
    }

    /**
     * Performs a drop table statement on the connection
     *
     * @param connection Connection instance
     * @param table      Table name
     */
    private void performDrop(@NotNull Connection connection, String table)
    {
        blueprint = createBlueprint(table);

        blueprint.drop();

        build(blueprint, connection);
    }

    /**
     * Chesks if a table Exists
     *
     * @param table
     * @param connection
     *
     * @return
     */
    public boolean hasTable(String table, @NotNull Connection connection)
    {
        Row row = new TableProcessor().single(connection.statement(connection.getSchemaGrammar().compileTableExists(
                table)));

        return ! row.isEmpty();
    }

    /**
     * @param table Table name
     */
    public void dropIfExists(String table)
    {
        for ( Connection connection : connections )
        {
            performDropIfExists(connection, table);
        }
    }

    private void performDropIfExists(Connection connection, String table)
    {
        blueprint = createBlueprint(table);

        blueprint.dropIfExists();

        build(blueprint, connection);
    }
}
