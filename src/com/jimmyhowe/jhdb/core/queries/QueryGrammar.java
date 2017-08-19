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

package com.jimmyhowe.jhdb.core.queries;

import com.jimmyhowe.jhdb.core.BaseGrammar;
import com.jimmyhowe.support.Str;
import com.jimmyhowe.support.stores.ObjectStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract Query Grammar
 *
 * Common query grammar methods go here...
 */
public class QueryGrammar extends BaseGrammar
{
    /**
     * @param query Query Builder Instance
     *
     * @return SQL Query
     */
    @NotNull
    public String compileSelect(@NotNull QueryBuilder query)
    {
        List<String> original = query.columns;

        if ( query.columns.isEmpty() )
        {
            query.columns = query.getDefaultSelectAll();
        }

        String sql = this.concatenate(this.compileComponents(query)).trim();

        query.columns = original;

        return sql;
    }

    /**
     * Concatenates the list into space separated string
     *
     * @param segments SQL Segments
     *
     * @return Reduced list
     */
    @NotNull
    public String concatenate(@NotNull List<String> segments)
    {
        return Str.implode(" ", segments.stream()
                                        .filter(s -> s != null && s != "")
                                        .collect(Collectors.toList()));
    }

    /**
     * Compile all the required components
     *
     * @param query Query Builder Instance
     *
     * @return Compiled Columns
     */
    @NotNull
    private List<String> compileComponents(@NotNull QueryBuilder query)
    {
        List<String> sql = new ArrayList<>();

        sql.add(compileAggregate(query));
        sql.add(compileColumns(query));
        sql.add(compileFrom(query));
        sql.add(compileJoins(query));
        sql.add(compileWheres(query));
        sql.add(compileGroups(query));
        sql.add(compileHavings(query));
        sql.add(compileOrders(query));
        sql.add(compileLimit(query));
        sql.add(compileOffset(query));
        sql.add(compileUnions(query));
        sql.add(compileLock(query));

        return sql;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled aggregate string
     */
    @Nullable
    protected String compileAggregate(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled columns string
     */
    @NotNull
    protected String compileColumns(@NotNull QueryBuilder query)
    {
        String select = query.distinct ? "SELECT DISTINCT " : "SELECT ";

        return select + columnize(query.columns);
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled from string
     */
    @NotNull
    protected String compileFrom(@NotNull QueryBuilder query)
    {
        return "FROM " + wrapTable(query.getTableName());
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled joins string
     */
    @Nullable
    protected String compileJoins(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled wheres string
     */
    @NotNull
    protected String compileWheres(@NotNull QueryBuilder query)
    {
        String whereStatements = "";

        ObjectStore whereGroups = query.wheres;

        if ( ! whereGroups.isEmpty() )
        {
            whereStatements = "WHERE ";

            for ( int i = 0; i < whereGroups.count(); i++ )
            {
                whereStatements += pad(whereGroups.data(i).toString());
            }
        }

        return whereStatements.trim();
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled groups string
     */
    @Nullable
    protected String compileGroups(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled havings string
     */
    @Nullable
    protected String compileHavings(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled order by strings
     */
    protected String compileOrders(@NotNull QueryBuilder query)
    {
        String orderBys = "";

        if ( ! query.orderBys.isEmpty() )
        {
            orderBys += query.orderBys.get(0).toString();
        }

        return orderBys;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled limit string
     */
    @Nullable
    protected String compileLimit(@NotNull QueryBuilder query)
    {
        return query.limit > 0 ? "LIMIT " + query.limit : null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled offset string
     */
    @Nullable
    protected String compileOffset(@NotNull QueryBuilder query)
    {
        return query.offset > 0 ? "OFFSET " + query.offset : null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled unions string
     */
    @Nullable
    protected String compileUnions(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query Query Builder Instance
     *
     * @return Compiled lock string
     */
    @Nullable
    protected String compileLock(QueryBuilder query)
    {
        return null;
    }

    /**
     * @param query SQL
     *
     * @return Compiled update string
     */
    @NotNull
    public String compileUpdate(@NotNull QueryBuilder query)
    {
        String table = wrapTable(query.getTableName());

        String columns = "SET " + pad(Str.toCsv(query.sets.getAllWithFormat("$k = '$v'")));

        String wheres = compileWheres(query);

        return (("UPDATE " + query.getTableName() + " " + columns) + wheres).trim();
    }

    /**
     * Compile Insert Statement
     *
     * INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...);
     *
     * @param query Query Builder Instance
     *
     * @return SQL
     */
    @NotNull
    public String compileInsert(@NotNull QueryBuilder query)
    {
        return String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                wrapTable(query.getTableName()),
                Str.toCsv(Arrays.asList(query.insertFields)),
                Str.toCsv(Arrays.asList(wrapValues(query.insertValues)))
        ).trim();
    }
}
