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

package com.jimmyhowe.jhdb.core.processors;

import com.jimmyhowe.jhdb.core.tables.columns.Column;
import com.jimmyhowe.jhdb.core.tables.columns.Columns;
import com.jimmyhowe.jhdb.core.tables.rows.Row;
import com.jimmyhowe.jhdb.core.tables.rows.Rows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Processes ResultSet's into Row and Rows Objects
 */
public class TableProcessor extends PostProcessor<Row, Rows>
{
    /**
     * Returns a single record
     *
     * @param resultSet Result Set
     *
     * @return Single Instance
     */
    @NotNull
    @Override
    public Row processSingle(@NotNull ResultSet resultSet) throws SQLException
    {
        if ( resultSet.next() )
        {
            return new Row(getColumns(resultSet));
        }

        return new Row();
    }

    /**
     * Returns a collection of records
     *
     * @param resultSet Result Set
     *
     * @return Collection Instance
     */
    @NotNull
    @Override
    public Rows processCollection(@NotNull ResultSet resultSet) throws SQLException
    {
        Rows rows = new Rows();

        while ( resultSet.next() )
        {
            rows.add(new Row(getColumns(resultSet)));
        }

        return rows;
    }

    /**
     * @param resultSet JDBC Result Set
     *
     * @return Columns Collection
     *
     * @throws SQLException SQL Exception
     */
    @NotNull
    private Columns getColumns(@NotNull ResultSet resultSet) throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();

        Columns columns = new Columns();

        for ( int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++ )
        {
            String columnName = metaData.getColumnName(columnIndex);
            Object value = resultSet.getObject(columnIndex);
            String sqlType = metaData.getColumnTypeName(columnIndex);

            columns.add(new Column(columnName, value));
        }

        return columns;
    }
}
