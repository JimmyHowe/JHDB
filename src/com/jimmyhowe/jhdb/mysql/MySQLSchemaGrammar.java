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

import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.schema.SchemaGrammar;
import com.jimmyhowe.jhdb.core.schema.Table;
import com.jimmyhowe.jhdb.core.tables.columns.Column;
import com.jimmyhowe.support.Str;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MySQL Specific Grammar
 */
public class MySQLSchemaGrammar extends SchemaGrammar
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

    private Object addPrimaryKeys(@NotNull Table table)
    {
        return ", PRIMARY KEY (id)";
    }

    private Object addForeignKeys(@NotNull Table table)
    {
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
    @Override
    public String drop(@NotNull Table table, String command, Connection connection)
    {
        return "DROP TABLE " + table.getTableName();
    }

    @Nullable
    @Override
    protected String compileBoolean(Column column)
    {
        return null;
    }

    @Nullable
    @Override
    protected String compileBinary(Column column)
    {
        return null;
    }

    /**
     * Compiles a timestamp column.
     *
     * @param column Column
     *
     * @return compiled column
     */
    @Nullable
    @Override
    protected String compileTimestamp(Column column)
    {
        return null;
    }

    /**
     * Compiles an integer column.
     *
     * @param column Column
     *
     * @return Compiled Column
     */
    @NotNull
    public String compileInteger(Column column)
    {
        StringBuilder builder = new StringBuilder(column.getName());

        builder.append(" ").append("INT");
        if ( column.isUnsigned() ) builder.append(" UNSIGNED");
        if ( column.isAutoIncrements() ) builder.append(" AUTO_INCREMENT");
        if ( ! column.isNullable() ) builder.append(" NOT NULL");
        if ( column.isUnique() ) builder.append(" UNIQUE");

        return builder.toString();
    }

    @NotNull
    @Override
    protected String getStringType(Column column)
    {
        // TODO: Fix this!!

//        return "VARCHAR(" + column.getParameter("length") + ")";

        return "VARCHAR(" + 255 + ")";
    }

    @Nullable
    @Override
    protected String getBinaryType(Column column)
    {
        return null;
    }

    @Nullable
    @Override
    protected String getBooleanType(Column column)
    {
        return null;
    }

    @Nullable
    @Override
    public String compileTableExists(String table)
    {
        return null;
    }

    @Override
    public String dropIfExists(Table table, String command, Connection connection)
    {
        return "DROP TABLE IF EXISTS " + table.getTableName();
    }
}
