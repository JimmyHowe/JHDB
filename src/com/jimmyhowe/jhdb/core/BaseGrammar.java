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

package com.jimmyhowe.jhdb.core;

import com.jimmyhowe.jhdb.core.utilities.Expression;
import com.jimmyhowe.support.Str;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base SQL Grammar
 */
public abstract class BaseGrammar
{
    /**
     * Grammar table prefix
     */
    protected String tablePrefix = "";

    /**
     * Wrap an array of values
     */
    public List<String> wrapArray(@NotNull List<String> values)
    {
        return values.stream()
                     .map(this::wrap)
                     .collect(Collectors.toList());
    }

    /**
     * @param table
     *
     * @return
     */
    @NotNull
    public String wrapTable(@NotNull Expression table)
    {
        return '"' + getValue(table) + '"';
    }


    /**
     * @param table
     */
    @NotNull
    public String wrapTable(String table)
    {
        return table;
    }

    /**
     * @param value Value to wrap
     *
     * @return Wrapped Value
     */
    @NotNull
    public String wrap(@NotNull String value, boolean prefixAlias)
    {
        // If the value being wrapped has a column alias we will need to separate out
        // the pieces so we can wrap each of the segments of the expression on it
        // own, and then joins them both back together with the "as" connector.
        if ( hasAliasedValue(value) )
        {
            return wrapAliasedValue(value, prefixAlias);
        }

        return wrapSegments(Arrays.asList(Str.explode(".", value)));
    }

    /**
     * Checks to see if the value has an alias
     *
     * @param value Value
     *
     * @return True if has alias
     */
    public boolean hasAliasedValue(@NotNull String value)
    {
        return Pattern.compile("(?i)\\s+as+\\s", Pattern.CASE_INSENSITIVE).matcher(value).find();
    }

    /**
     * @param expression
     *
     * @return
     */
    public String wrap(@NotNull Expression expression)
    {
        return getValue(expression);
    }

    /**
     * Default wrap with no prefix
     *
     * @param value
     *
     * @return
     */
    @NotNull
    public String wrap(Object value)
    {
        return wrap(String.valueOf(value), false);
    }

    /**
     * @param value
     * @param prefixAlias
     *
     * @return
     */
    @NotNull
    public String wrapAliasedValue(@NotNull String value, boolean prefixAlias)
    {
        String[] segments = value.split("(?i)\\s+as+\\s");

        // If we are wrapping a table we need to prefix the alias with the table prefix
        // as well in order to generate proper syntax. If this is a column of course
        // no prefix is necessary. The condition will be true when from wrapTable.
        if ( prefixAlias )
        {
            segments[1] = tablePrefix + segments[1];
        }

        return wrap(segments[0]) + " AS " + wrapValue(segments[1]);
    }

    /**
     * @param value
     *
     * @return
     */
    @NotNull
    public String wrapAliasedValue(@NotNull String value)
    {
        return wrapAliasedValue(value, false);
    }

    /**
     * Wraps a list of segments
     *
     * @param segments List of Segments
     *
     * @return Wrapped Segments
     */
    @NotNull
    public String wrapSegments(@NotNull List<String> segments)
    {
//        return collect($segments)->map(function ($segment, $key) use ($segments) {
//        return $key == 0 && count($segments) > 1
//                ? $this->wrapTable($segment)
//                : $this->wrapValue($segment);
//        })->implode('.');

        List<String> newSegments = new ArrayList<>();

        for ( int i = 0; i < segments.size(); i++ )
        {
            if ( i == 0 && segments.size() > 1 )
            {
                newSegments.add(this.wrapTable(segments.get(i)));
            } else
            {
                newSegments.add(this.wrapValue(segments.get(i)));
            }
        }

        return Str.implode(".", newSegments);
    }

    /**
     * @param values
     *
     * @return
     */
    @NotNull
    public String[] wrapValues(@NotNull Object... values)
    {
        String[] stringValues = new String[values.length];

        for ( int i = 0; i < values.length; i++ )
        {
            stringValues[i] = wrapValue((values[i].toString()));
        }

        return stringValues;
    }

    /**
     * Wraps the passed string in doubles-quotes, and if the string contains any double-quotes then it doubles the
     * quotes... simple.
     *
     * @param value Any string value
     *
     * @return
     */
    @NotNull
    public String wrapValue(@NotNull String value)
    {
        if ( ! Objects.equals(value, "*") )
        {
            return "\"" + value.replace("\"", "\"\"") + "\"";
//            return "`" + value.replace("\"", "\"\"") + "`";
        }

        return value;
    }


    /**
     * Convert an array of column names into a delimited string. This method calls the implode method from the Str
     * library. Each of the passed strings are separated into columns after being processed by the wrap function.
     *
     * @param columns List of columns
     *
     * @return string
     */
    @NotNull
    public String columnize(@NotNull List<String> columns)
    {
        return Str.implode(", ", columns.stream().map(this::wrap).collect(Collectors.toList()));
    }

    /**
     * Create query parameter place-holders for an array.
     *
     * @param values List of values
     *
     * @return string
     */
    @NotNull
    public String parameterize(@NotNull List<String> values)
    {
        return Str.implode(", ", values.stream().map(this::parameter).collect(Collectors.toList()));
    }

    /**
     * Get the appropriate query parameter place-holder for a value.
     *
     * @param value Expression
     *
     * @return string
     */
    public String parameter(@NotNull Expression value)
    {
        return getValue(value);
    }

    /**
     * @param columns List of Columns
     *
     * @return List of Wrapped Strings
     */
    public List<String> wrap(@NotNull List<Object> columns)
    {
        return columns.stream()
                      .map(this::wrap)
                      .collect(Collectors.toList());
    }

    /**
     * Pad method from ExpressDB
     *
     * @param statement SQL Statement
     *
     * @return Padded String
     */
    @NotNull
    public String pad(@NotNull String statement)
    {
        return statement != "" ? statement.trim() + " " : "";
    }

    /**
     * Get value of expression
     *
     * @param expression Expression Object
     *
     * @return Expression Value
     */
    public String getValue(@NotNull Expression expression)
    {
        return expression.getValue();
    }

    /**
     * Get the appropriate query parameter place-holder for a value.
     *
     * @param value Value
     *
     * @return string
     */
    @NotNull
    public String parameter(String value)
    {
        return "?";
    }

    /**
     * Get the format for database stored dates.
     *
     * @return string
     */
    @NotNull
    public String getDateFormat()
    {
        return "Y-m-d H:i:s";
    }

    /**
     * Get the grammar's table prefix.
     *
     * @return string
     */
    public String getTablePrefix()
    {
        return tablePrefix;
    }

    /**
     * Set the grammar's table prefix.
     *
     * @param prefix Table prefix
     *
     * @return this
     */
    @NotNull
    public BaseGrammar setTablePrefix(String prefix)
    {
        tablePrefix = prefix;

        return this;
    }
}

