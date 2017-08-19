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
import com.jimmyhowe.jhdb.core.tables.columns.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Table Blueprint
 */
public class Table
{
    /**
     * Whether to make the table temporary.
     */
    public final boolean temporary = false;

    /**
     * The columns that should be added to the table.
     */
    protected final List<Column> columns = new ArrayList<>();

    /**
     * The commands that should be run for the table.
     */
    protected final List<String> commands = new ArrayList<>();

    /**
     * Table name
     */
    private final String table;

    /**
     * The storage engine that should be used for the table.
     */
    public String engine;

    /**
     * The default character set that should be used for the table.
     */
    public String charset;

    /**
     * The collation that should be used for the table.
     */
    public String collation;

    /**
     * @param table Table Name
     */
    public Table(String table)
    {
        this.table = table;
    }

    /**
     * @param table    Table Name
     * @param callback TableBlueprint Callback
     */
    public Table(String table, @NotNull Blueprint callback)
    {
        this.table = table;

        callback.build(this);
    }

    /**
     * Add a column with config options
     *
     * @param type   Type of column
     * @param name   Name of column
     * @param config Configuration options
     */
    public Column addColumn(String type, String name, String... config)
    {
        columns.add(new Column(type, name, config));

        return columns.get(columns.size() - 1);
    }

    /**
     * Add a binary column
     *
     * @param name Column name
     */
    public Column binary(String name)
    {
        return this.addColumn("binary", name);
    }

    /**
     * Add a boolean column
     *
     * @param name
     */
    public Column bool(String name)
    {
        return this.addColumn("boolean", name);
    }

    /**
     * Create a new auto-incrementing integer (4-byte) column on the table.
     *
     * @param name Name of the column
     */
    public Column increments(String name)
    {
        return this.unsignedInteger(name, true);
    }

    /**
     * @param name The name of the field to create
     */
    public Column unsignedInteger(String name)
    {
        return this.unsignedInteger(name, false);
    }

    /**
     * @param name           The name of the field to create
     * @param autoIncrements Do you want to autoIncrement this field?
     */
    public Column unsignedInteger(String name, boolean autoIncrements)
    {
        return this.integer(name, autoIncrements, true);
    }

    /**
     * @param name           Name of the field to create
     * @param autoIncrements Auto increment
     * @param unsigned       unsigned?
     */
    public Column integer(String name, boolean autoIncrements, boolean unsigned)
    {
        String[] config = new String[2];

        config[0] = autoIncrements ? "autoIncrements" : null;
        config[1] = unsigned ? "unsigned" : null;

        return this.addColumn("integer", name, config);
    }

    /**
     * @param name Name of the field to create
     */
    public Column integer(String name)
    {
        return this.addColumn("integer", name);
    }

    /**
     * @param name   Field Name
     * @param length String length
     */
    public Column string(String name, int length)
    {
        return this.addColumn("string", name, "length=" + length);
    }

    /**
     * @param name Field name
     */
    public Column string(String name)
    {
        return this.string(name, 255);
    }

    /**
     * TIMESTAMP equivalent for the database.
     *
     * @param name
     */
    private void timestamp(String name)
    {
        this.addColumn("timestamp", name);
    }

    /**
     * Adds nullable created_at and updated_at columns.
     */
    public void timestamps()
    {
        this.timestamp("created_at");
        this.timestamp("updated_at");
    }

    /**
     * Create
     */
    public void create()
    {
        this.addCommand("create");
    }

    /**
     * Drop Table
     */
    public void drop()
    {
        this.addCommand("drop");
    }

    /**
     * @param command Command
     */
    private void addCommand(String command)
    {
        commands.add(command);
    }

    /**
     * @param connection    Connection
     * @param schemaGrammar SQL Grammar
     */
    public void build(@NotNull Connection connection, @NotNull SchemaGrammar schemaGrammar)
    {
        toSQL(connection, schemaGrammar).forEach(connection::execute);
    }

    /**
     * @param connection    Connection
     * @param schemaGrammar SQL Grammar
     *
     * @return List of SQL Statements
     */
    @NotNull
    public List<String> toSQL(Connection connection, @NotNull SchemaGrammar schemaGrammar)
    {
        List<String> statements = new ArrayList<>();

        for ( String command : commands )
        {
            switch ( command )
            {
                case "create":
                    statements.add(schemaGrammar.create(this, command, connection));
                    break;
                case "drop":
                    statements.add(schemaGrammar.drop(this, command, connection));
                case "dropIfExists":
                    statements.add(schemaGrammar.dropIfExists(this, command, connection));
            }
        }

        return statements;
    }

    /**
     * The table the blueprint describes.
     */
    public String getTableName()
    {
        return table;
    }

    /**
     * @return Columns as List
     */
    @NotNull
    public List<Column> getColumns()
    {
        return columns;
    }

    /**
     * @return Columns as formatted strings
     *
     * @deprecated No SQL as conflicts with autoincrements
     */
    @Nullable
    public List<String> getColumnsAsStrings()
    {
//        List<String> strings = new ArrayList<>();
//
//        for ( int i = 0; i < getColumns().size(); i++ )
//        {
//            strings.add(columns.get(i).toSql());
//        }
//
//        return strings;

        return null;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "TableBlueprint{" +
                " table='" + getTableName() + '\'' +
                ", columns=" + columns +
                ", commands=" + commands +
                ", engine='" + engine + '\'' +
                ", charset='" + charset + '\'' +
                ", collation='" + collation + '\'' +
                ", temporary=" + temporary +
                '}';
    }

    public void dropIfExists()
    {
        this.addCommand("dropIfExists");
    }
}
