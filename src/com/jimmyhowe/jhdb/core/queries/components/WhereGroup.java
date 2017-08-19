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

package com.jimmyhowe.jhdb.core.queries.components;

import com.jimmyhowe.support.Str;
import com.jimmyhowe.support.stores.ObjectStore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WhereGroup extends ObjectStore
{
    public WhereGroup()
    {

    }

    public WhereGroup(Where where)
    {
        this.data().add(where);
    }

    public WhereGroup(@NotNull Where[] wheres)
    {
        for ( int i = 0; i < wheres.length; i++ )
        {
            this.data().add(wheres[i]);
        }
    }

    @NotNull
    @Override
    public String toString()
    {
        List<String> list = new ArrayList<>();

        for ( int i = 0; i < this.data().size(); i++ )
        {
            list.add(this.data().get(i).toString());
        }

        return list.size() == 1 ? Str.toSpaceSeparated(list) : "( " + Str.toSpaceSeparated(list) + " )";
    }
}
