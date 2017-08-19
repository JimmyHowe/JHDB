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

import com.jimmyhowe.support.collections.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract Post Processor
 *
 * The Post Processor is used to process the results of the ResultSet into a single object or a collection of objects
 * for multiple rows.
 *
 * @param <S> Single Row Object
 * @param <C> Collection Object
 */
public abstract class PostProcessor<S, C extends Collection<S>>
{
    /**
     * Returns a single record
     *
     * @param resultSet Result Set
     *
     * @return Single Instance
     */
    @Nullable
    public S single(ResultSet resultSet)
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
    public abstract S processSingle(ResultSet resultSet) throws SQLException;

    /**
     * Returns a collection of records
     *
     * @param resultSet Result Set
     *
     * @return Collection Instance
     */
    @Nullable
    public C collection(ResultSet resultSet)
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
    public abstract C processCollection(ResultSet resultSet) throws SQLException;
}
