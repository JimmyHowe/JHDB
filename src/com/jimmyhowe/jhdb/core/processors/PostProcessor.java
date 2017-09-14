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

import com.jimmyhowe.jhdb.core.tables.rows.Row;
import com.jimmyhowe.jhdb.core.tables.rows.Rows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract Post Processor
 *
 * The Post Processor is used to process the results of the ResultSet into a single object or a collection of objects
 * for multiple rows.
 */
public abstract class PostProcessor
{
    /**
     * Returns a single record
     *
     * @param resultSet Result Set
     *
     * @return Single Instance
     */
    @Nullable
    public Row single(ResultSet resultSet)
    {
        try
        {
            return processSingle(resultSet);
        } catch ( Exception e )
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a single record
     *
     * @param resultSet Result Set
     *
     * @return Single Instance
     *
     * @throws SQLException When can retrieve results
     */
    @NotNull
    public abstract Row processSingle(ResultSet resultSet) throws SQLException;

    /**
     * Returns a collection of records
     *
     * @param resultSet Result Set
     *
     * @return Collection Instance
     */
    @Nullable
    public Rows collection(@NotNull ResultSet resultSet)
    {
        try
        {
            return processCollection(resultSet);
        } catch ( SQLException e )
        {
            System.out.println("Collection had an SQL Exception");
            e.printStackTrace();
        } catch ( NullPointerException e )
        {
            System.out.println("Collection was passed a null result set");
            e.printStackTrace();
        }

        System.out.println("PostProcessor: Collection Failed");
        return null;
    }

    /**
     * Returns a collection of records
     *
     * @param resultSet Result Set
     *
     * @return Collection Instance
     *
     * @throws SQLException When can retrieve results
     */
    @NotNull
    public abstract Rows processCollection(ResultSet resultSet) throws SQLException;
}
