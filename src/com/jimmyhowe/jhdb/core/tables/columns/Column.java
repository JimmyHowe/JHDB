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

package com.jimmyhowe.jhdb.core.tables.columns;

import com.jimmyhowe.support.Str;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Column
 *
 * Contains Information about a column Its used when building a column from the com.jimmyhowe.jhdb.schema builder Or
 * storing Values when retrieving columns from the DB
 */
public class Column
{
    /**
     * Type of name
     */
    private String type;

    /**
     * Name of the name
     */
    private String name;

    /**
     * Value
     */
    @Nullable
    private Object value = null;

    /**
     * Original Parameters
     */
    @NotNull
    private Map<String, Object> originalParameters = new HashMap<>();

    /**
     * Updated Parameters
     */
    @NotNull
    private Map<String, Boolean> updatedParameters = new HashMap<>();

    /**
     * Column
     */
    public Column()
    {

    }

    /**
     * @param name  Column Name
     * @param value Column Value
     */
    public Column(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * @param type       Column type
     * @param name       Column name
     * @param parameters Column Parameters
     */
    public Column(String type, String name, @NotNull String... parameters)
    {
        this.type = type;
        this.name = name;

        parseParameters(parameters);
    }

    /**
     * AutoIncrements Builder
     *
     * @return this
     */
    @NotNull
    public Column autoIncrements()
    {
        originalParameters.put("autoIncrements", true);

        return this;
    }

    /**
     * @return Auto Increments
     */
    public boolean isAutoIncrements()
    {
        return (boolean) originalParameters.getOrDefault("autoIncrements", false);
    }

    /**
     * Unsigned Builder
     *
     * @return this
     */
    @NotNull
    public Column unsigned()
    {
        originalParameters.put("unsigned", true);

        return this;
    }

    /**
     * @return Is Unsigned
     */
    public boolean isUnsigned()
    {
        return (boolean) originalParameters.getOrDefault("unsigned", false);
    }

    /**
     * Make Nullable
     *
     * @return this
     */
    @NotNull
    public Column nullable()
    {
        originalParameters.put("nullable", true);

        return this;
    }

    /**
     * @return Nullable
     */
    public boolean isNullable()
    {
        return (boolean) originalParameters.getOrDefault("nullable", false);
    }

    /**
     * Make Unique
     *
     * @return this
     */
    @NotNull
    public Column unique()
    {
        originalParameters.put("unique", true);

        return this;
    }

    /**
     * @return If the column is unique
     */
    public boolean isUnique()
    {
        return (boolean) originalParameters.getOrDefault("unique", false);
    }

    /**
     * Make Primary
     *
     * @return this
     */
    @NotNull
    public Column primary()
    {
        originalParameters.put("primary", true);

        return this;
    }

    /**
     * @return Column is primary
     */
    public boolean isPrimary()
    {
        return (boolean) originalParameters.getOrDefault("primary", false);
    }

    /**
     * Parses the Config Array
     *
     * @param parameters Parameters
     */
    private void parseParameters(@NotNull String[] parameters)
    {
        for ( String parameter : parameters )
        {
            if ( parameter != null )
            {
                // if the parameter has a value
                if ( parameter.contains("=") )
                {
                    String[] segments = Str.explode("=", parameter);

                    originalParameters.put(segments[0].trim(), segments[1].trim());
                } else
                {
                    originalParameters.put(parameter, true);
                }
            }
        }
    }

    public boolean hasParameter(String parameter)
    {
        return originalParameters.containsKey(parameter);
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    @Nullable
    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

//    /**
//     * @return String Length
//     */
//    public int getLength()
//    {
//        String length = (String) originalParameters.getOrDefault("length", SchemaBuilder.defaultStringLength);
//
//        return Integer.parseInt(length);
//    }

    @NotNull
    @Override
    public String toString()
    {
        return "Column{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", originalParameters=" + originalParameters +
                ", updatedParameters=" + updatedParameters +
                '}';
    }
}
