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

package com.jimmyhowe.jhdb;

import com.jimmyhowe.colorconsole.Console;
import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.jhdb.mysql.MySQLPlugin;

public class Main
{
    public static void main(String[] args)
    {
        DB.onCantConnect(() -> {
            Console.red("Cant Connect to Database");
            System.exit(0);
        });

        MySQLPlugin mySQLPlugin = new MySQLPlugin();

        DB.use(mySQLPlugin);

//        Schema.create("users", new Blueprint()
//        {
//            @Override
//            public void build(Table table)
//            {
//                table.increments("id");
//                table.string("name");
//            }
//        });

        DB.execute("DROP TABLE users");

        DB.execute(
                "CREATE TABLE users (id INT UNSIGNED NOT NULL AUTO_INCREMENT, name VARCHAR(50) NOT NULL, PRIMARY KEY (id))");

        DB.select("SELECT * FROM users");

        int result = DB.table("users").insertInto("name").values("JimmyHowe");

        System.out.println(result);
    }
}
