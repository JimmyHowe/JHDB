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

import com.jimmyhowe.jhdb.core.BaseGrammar;
import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.tables.columns.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Template for Schema Grammar
 */
public abstract class SchemaGrammar extends BaseGrammar
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
    @Nullable
    public abstract String create(Table table, String command, Connection connection);

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
    public abstract String drop(Table table, String command, Connection connection);

    /**
     * Compiles list of table in string format.
     *
     * @param table List of Columns
     *
     * @return List of table in string format
     */
    @NotNull
    protected List<String> compileColumns(@NotNull Table table)
    {
        List<String> compiledColumns = new ArrayList<>();

        for ( Column column : table.getColumns() )
        {
            switch ( column.getType().toLowerCase() )
            {
                case "integer":
                    compiledColumns.add(compileInteger(column));
                    break;
                case "string":
                    compiledColumns.add(compileString(column));
                    break;
                case "binary":
                    compiledColumns.add(compileBinary(column));
                    break;
                case "boolean":
                    compiledColumns.add(compileBoolean(column));
                    break;
                case "timestamp":
                    compiledColumns.add(compileTimestamp(column));
                    break;
                default:
                    System.out.println("SchemaGrammar: cant create column: " + column.getType().toUpperCase());
            }
        }

        return compiledColumns;
    }

    @Nullable
    protected abstract String compileBinary(Column column);

    @Nullable
    protected abstract String compileBoolean(Column column);

    /**
     * Compiles a timestamp column.
     *
     * @param column Column
     *
     * @return compiled column
     */
    @Nullable
    protected abstract String compileTimestamp(Column column);

    /**
     * Compiles an integer column
     *
     * @param column Column
     *
     * @return Compiled integer
     */
    @NotNull
    protected abstract String compileInteger(Column column);

    /**
     * Compiles a string column.
     *
     * @param column Column
     *
     * @return Compiled Column
     */
    @NotNull
    protected String compileString(@NotNull Column column)
    {
//        StringBuilder builder = new StringBuilder(this.wrap(column.getName()));
        StringBuilder builder = new StringBuilder(column.getName());

        builder.append(" ").append(this.getStringType(column));

        if ( ! column.isNullable() ) builder.append(" NOT NULL");
        if ( column.isUnique() ) builder.append(" UNIQUE");

        return builder.toString();
    }

    @NotNull
    protected abstract String getStringType(Column column);

    @Nullable
    protected abstract String getBinaryType(Column column);

    @Nullable
    protected abstract String getBooleanType(Column column);

    /**
     * Compile Table Exists SQL
     *
     * @param table
     *
     * @return Table Exists SQL
     */
    @Nullable
    public abstract String compileTableExists(String table);

    /**
     * @param table      Table name
     * @param command    Command
     * @param connection Connection
     *
     * @return Query String
     */
    public abstract String dropIfExists(Table table, String command, Connection connection);
}
