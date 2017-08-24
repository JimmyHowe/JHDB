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

import com.jimmyhowe.colorconsole.Console;
import com.jimmyhowe.jhdb.core.Connection;
import com.jimmyhowe.jhdb.core.processors.PostProcessor;
import com.jimmyhowe.jhdb.core.queries.components.*;
import com.jimmyhowe.jhdb.core.utilities.Expression;
import com.jimmyhowe.support.collections.Collection;
import com.jimmyhowe.support.stores.KeyValueStore;
import com.jimmyhowe.support.stores.ObjectStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Query Builder
 */
public class QueryBuilder
{
    /**
     * Where Statements
     */
    public final ObjectStore wheres = new ObjectStore();

    /**
     * Order By Components
     */
    public final List<OrderBy> orderBys = new ArrayList<>();

    /**
     * Update SET Component
     */
    public final KeyValueStore sets = new KeyValueStore();

    /**
     * Database Connector Instance
     */
    protected final Connection connection;

    /**
     * Query Grammar Instance
     */
    protected final QueryGrammar queryGrammar;

    /**
     * Post Processor Instance
     */
    protected final PostProcessor processor;

    /**
     * Columns
     */
    public List<String> columns = getDefaultSelectAll();

    /**
     * Limit
     */
    public int limit = 0;

    /**
     * Distinct
     */
    public boolean distinct;

    public int offset;

    public String[] insertFields;

    public Object[] insertValues;

    private String tableName;

    private QueryGrammar grammar;

    /**
     * @param connection   Database Connector
     * @param queryGrammar SQL Grammar
     * @param processor    Post Processor
     */
    public QueryBuilder(Connection connection, QueryGrammar queryGrammar, PostProcessor processor)
    {
        this.connection = connection;
        this.queryGrammar = queryGrammar;
        this.processor = processor;
    }

    /**
     * Sets the Table Name
     *
     * @param table Table Name
     *
     * @return This query builder instance
     */
    @NotNull
    public QueryBuilder from(String table)
    {
        this.tableName = table;

        return this;
    }

    /**
     * @return Table name
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @return Columns list with default select all
     */
    @NotNull
    public ArrayList<String> getDefaultSelectAll()
    {
        return new ArrayList<>(Collections.singletonList("*"));
    }

    /**
     * Selects Builder
     *
     * @return Query builder instance
     */
    @NotNull
    public QueryBuilder select()
    {
        this.columns = getDefaultSelectAll();

        return this;
    }

    /**
     * Selects Builder
     *
     * @return This query builder instance
     */
    @NotNull
    public QueryBuilder select(String... columns)
    {
        this.columns = new ArrayList<>(Arrays.asList(columns));

        return this;
    }

    /**
     * @param raw Raw SQL Statement
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder select(@NotNull Expression... raw)
    {
        this.columns = new ArrayList<>();

        for ( int i = 0; i < raw.length; i++ )
        {
            this.columns.add(raw[i].toString());
        }

        return this;
    }

    /**
     * Where Statement
     *
     * @param column Column name
     * @param value  Value
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder where(String column, String operator, Object value)
    {
        this.wheres.put(new WhereGroup(new Where(column, operator, value)));

        return this;
    }

    /**
     * Where Statement
     *
     * @param column Column name
     * @param value  Value
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder where(String column, Object value)
    {
        this.wheres.put(new WhereGroup(new Where(column, value)));

        return this;
    }

    /**
     * @param wheres Multiple Where Statements
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder where(@NotNull Where... wheres)
    {
        this.wheres.put(new WhereGroup(wheres));

        return this;
    }

    /**
     * Where Not Statement
     *
     * @param column Column name
     * @param value  Value
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder whereNot(String column, String value)
    {
        this.wheres.put(new WhereGroup(new Where(column, "!=", value)));

        return this;
    }

    /**
     * @param column   Column Name
     * @param operator Operator
     * @param value    Value
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder andWhere(String column, String operator, Object value)
    {
        this.wheres.put(new AndWhereGroup(new Where(column, operator, value)));

        return this;
    }

    /**
     * @param column Column Names
     * @param value  Value
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder andWhere(String column, Object value)
    {
        this.wheres.put(new AndWhereGroup(new Where(column, value)));

        return this;
    }

    /**
     * @param wheres Where Statements
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder andWhere(@NotNull Where... wheres)
    {
        this.wheres.put(new AndWhereGroup(wheres));

        return this;
    }

    /**
     * @param column   Column Name
     * @param operator Operator
     * @param value    Value
     *
     * @return This query builder instance
     */
    @NotNull
    public QueryBuilder orWhere(String column, String operator, Object value)
    {
        this.wheres.put(new OrWhereGroup(new Where(column, operator, value)));

        return this;
    }

    /**
     * @param column Column Name
     * @param value  Value
     *
     * @return This query builder instance
     */
    @NotNull
    public QueryBuilder orWhere(String column, Object value)
    {
        this.wheres.put(new OrWhereGroup(new Where(column, value)));

        return this;
    }

    /**
     * @param wheres Where Statements
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder orWhere(@NotNull Where... wheres)
    {
        this.wheres.put(new OrWhereGroup(wheres));

        return this;
    }

    /**
     * Order By
     *
     * @param column    Column name
     * @param direction Direction
     */
    @NotNull
    public QueryBuilder orderBy(String column, String direction)
    {
        this.orderBys.add(new OrderBy(column, direction));

        return this;
    }

    /**
     * Order By Asc
     *
     * @param column Column name
     */
    @NotNull
    public QueryBuilder orderBy(String column)
    {
        return this.orderBy(column, "ASC");
    }

    /**
     * Order By Desc
     *
     * @param column Column name
     */
    @NotNull
    public QueryBuilder orderByDesc(String column)
    {
        return this.orderBy(column, "DESC");
    }

    /**
     * Distinct Select
     *
     * @return QueryBuilder
     */
    @NotNull
    public QueryBuilder distinct()
    {
        this.distinct = true;

        return this;
    }

    /**
     * @param column Column Name
     * @param value  Value
     *
     * @return QueryBuilder Instance
     */
    @NotNull
    public QueryBuilder set(String column, Object value)
    {
        this.sets.add(column, value);

        return this;
    }

    /**
     * @param raw Raw Expression
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder addSelect(@NotNull Expression raw)
    {
        this.columns.add(raw.toString());

        return this;
    }

    /**
     * Execute the query as a "select" statement.
     *
     * @param columns Column names
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Collection> T get(String... columns)
    {
        List<String> original = this.columns;

        if ( original.isEmpty() )
        {
            this.columns = Arrays.asList(columns);
        }

//        ResultSet results = this.processor.processSelect(this, this.runSelect());

        ResultSet results = this.runSelect();

//        System.out.println(results);

        this.columns = original;

        return (T) this.processor.collection(results);
    }

    /**
     * @param columns Columns
     *
     * @return First record from db
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> T first(String... columns)
    {
        return (T) this.take(1).get(columns).first();
    }

    /**
     * @param limit Amount to Limit
     *
     * @return Query Builder
     */
    @NotNull
    public QueryBuilder limit(int limit)
    {
        this.limit = limit;

        return this;
    }

    /**
     * Alias to the limit method
     *
     * @param limit Limit number
     *
     * @return QueryBuilderL
     */
    @NotNull
    public QueryBuilder take(int limit)
    {
        return this.limit(limit);
    }

    /**
     * Sets the Offset
     *
     * @param offset Offset
     *
     * @return This query builder instance
     */
    @NotNull
    public QueryBuilder offset(int offset)
    {
        this.offset = offset;

        return this;
    }

    /**
     * List with primary key
     *
     * @param primaryKey Primary key
     * @param column     Column name
     */
    @Nullable
    public Collection list(String primaryKey, String column)
    {
        return get(primaryKey, column);
    }

    /**
     * Return a list of ID, and Column Name
     *
     * @param column Column name
     */
    @Nullable
    public Collection list(String column)
    {
        return list("id", column);
    }

    /**
     * Run the query as a "select" statement against the connection.
     *
     * @return array
     */
    @Nullable
    protected ResultSet runSelect()
    {
        return this.connection.select(this.toSql());
    }

    /**
     * Get the SQL representation of the query.
     *
     * @return string
     */
    @NotNull
    public String toSql()
    {
        Console.green(String.valueOf(queryGrammar));

        return this.queryGrammar.compileSelect(this);
    }

    /**
     * Checks if the Table Exists
     *
     * @return True of False if Table exists
     */
    public boolean exists()
    {
//        Row row = new TableProcessor().single(this.connection.statement(
//                this.connection.getSchemaGrammar().compileTableExists(connection.)
//        ));
//
//        return ! row.isEmpty();

        return false;
    }

    /**
     * Gets Post Processor Instance.
     *
     * @return Post Processor Instance.
     */
    public PostProcessor getProcessor()
    {
        return processor;
    }

    /**
     * Performs an UPDATE statement.
     *
     * @return Number of affected rows
     */
    public int update()
    {
        String sql = this.queryGrammar.compileUpdate(this);

        return this.connection.update(sql);
    }

    public QueryGrammar getGrammar()
    {
        return grammar;
    }

    @NotNull
    public QueryBuilder insertInto(String... fields)
    {
        this.insertFields = fields;

        return this;
    }

    public int values(Object... values)
    {
        this.insertValues = values;

        String sql = this.queryGrammar.compileInsert(this);

        return this.connection.insert(sql);
    }
}
