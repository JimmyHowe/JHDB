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

package com.jimmyhowe.jhdb.core.tables.rows;

import com.jimmyhowe.jhdb.core.tables.columns.Column;
import com.jimmyhowe.jhdb.core.tables.columns.Columns;
import com.jimmyhowe.support.collections.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Table Row
 */
public class Row extends Collection<Column>
{
    /**
     * Default Constructor
     */
    public Row()
    {
    }

    /**
     * Build with Column Collection
     *
     * @param columns Columns
     */
    public Row(@NotNull Columns columns)
    {
        data().addAll(columns.data());
    }

    /**
     * Return Column by Field
     *
     * @param field Field Name
     */
    @Nullable
    public Column column(String field)
    {
        return this.findByField(field);
    }

    /**
     * Find by Field
     *
     * @param field Field Name
     */
    private Column findByField(String field)
    {
        for ( int i = 0; i < this.data().size(); i++ )
        {
            if ( this.data(i).getName().equals(field) )
            {
                return this.data(i);
            }
        }

        return null;
    }

    /**
     * Returns data as Column Collection
     */
    @NotNull
    public Columns columns()
    {
        return new Columns(this.data());
    }

    /**
     * @return Returns Row as String
     */
    @Override
    public String toString()
    {
        return data().toString();
    }

    public boolean isEmpty()
    {
        return data().isEmpty();
    }
}
