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

package com.jimmyhowe.jhdb.sqlite;

import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import com.jimmyhowe.jhdb.core.schema.Table;
import com.jimmyhowe.jhdb.core.tables.columns.Column;
import com.jimmyhowe.support.Str;
import org.jetbrains.annotations.NotNull;

/**
 * SQLite Specific Grammar
 */
public class SQLiteSchemaGrammar extends SchemaGrammar
{
    /**
     * Compiles a create table query.
     *
     * @param table      Table Blueprint
     * @param command    Command
     * @param connection Connection
     *
     * @return SQL Statement
     */
    public String create(@NotNull Table table, String command, Connection connection)
    {
        String sql = String.format(
                "%s TABLE %s (%s%s%s)",
                table.temporary ? "CREATE TEMPORARY" : "CREATE",
                this.wrapTable(table.getTableName()),
                Str.toCsv(compileColumns(table)),
                this.addForeignKeys(table),
                this.addPrimaryKeys(table)
        );

        return sql;
    }

    /**
     * @param table
     *
     * @return
     */
    private String addForeignKeys(Table table)
    {
        // FOREIGN KEY(trackartist) REFERENCES artist(artistid)

        return "";
    }

    /**
     * Compiles a drop table query.
     *
     * @param table      Table Blueprint
     * @param command    Command
     * @param connection Connection
     *
     * @return SQL Statement
     */
    @NotNull
    public String drop(@NotNull Table table, String command, Connection connection)
    {
        return "DROP TABLE " + table.getTableName();
    }

    /**
     * @param table
     *
     * @return
     */
    private String addPrimaryKeys(Table table)
    {
        return "";
    }

    /**
     * Compiles an integer column.
     *
     * @param column Column
     *
     * @return Compiled Column
     */
    @NotNull
    public String compileInteger(@NotNull Column column)
    {
        StringBuilder builder = new StringBuilder(wrap(column.getName()));

        builder.append(" ").append(column.getType().toUpperCase());

        if ( column.isAutoIncrements() )
        {
            if ( column.isAutoIncrements() ) builder.append(" PRIMARY KEY AUTOINCREMENT");
        } else
        {
            if ( column.isUnsigned() ) builder.append(" UNSIGNED");
            if ( ! column.isNullable() ) builder.append(" NOT NULL");
            if ( column.isPrimary() ) builder.append(" PRIMARY KEY");
            if ( column.isUnique() ) builder.append(" UNIQUE");
        }

        return builder.toString();
    }

    /**
     * Compiles a timestamp column.
     *
     * @param column Column
     *
     * @return compiled column
     */
    @NotNull
    @Override
    protected String compileTimestamp(@NotNull Column column)
    {
        return wrap(column.getName()) + " " + getTimestampType();
    }

    @NotNull
    @Override
    protected String compileBoolean(@NotNull Column column)
    {
        return wrap(column.getName()) + " " + getBooleanType(column);
    }

    @NotNull
    @Override
    protected String compileBinary(@NotNull Column column)
    {
        return wrap(column.getName()) + " " + getBinaryType(column);
    }

    /**
     * @param column
     *
     * @return String Type
     */
    @NotNull
    @Override
    protected String getStringType(Column column)
    {
        return "VARCHAR";
    }

    @NotNull
    @Override
    protected String getBinaryType(Column column)
    {
        return "BLOB";
    }

    @NotNull
    @Override
    protected String getBooleanType(Column column)
    {
        return "TINYINT(1)";
    }

    /**
     * @return Timestamp Type
     */
    @NotNull
    protected String getTimestampType()
    {
        return "DATETIME DEFAULT CURRENT_TIMESTAMP";
    }

    /**
     * @param table
     *
     * @return Compiled table exists string
     */
    @NotNull
    public String compileTableExists(String table)
    {
        return "SELECT * FROM sqlite_master WHERE type = 'table' AND name = " + wrapTable(table);
    }
}
