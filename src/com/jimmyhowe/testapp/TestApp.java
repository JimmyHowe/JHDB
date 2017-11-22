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

package com.jimmyhowe.testapp;

import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.jhdb.core.schema.Schema;
import com.jimmyhowe.jhdb.sqlite.SQLitePlugin;

public class TestApp
{
    public static void main(String[] args)
    {
        DB.register("test", new SQLitePlugin().inMemory());

        System.out.println(DB.getConnectionList());

        Schema.connection("test").create("users", table -> {
            table.increments("id");
            table.string("firstName");
            table.string("lastName");
        });

        DB.connection("test").table("users").insertInto("firstName", "lastName").values("Jimmy", "Howe");

//        Rows rows = DB.table("users").where("firstName", "Jimmy").get();
//
//        System.out.println(rows);
    }
}
