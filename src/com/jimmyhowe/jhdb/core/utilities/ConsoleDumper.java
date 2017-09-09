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

package com.jimmyhowe.jhdb.core.utilities;

import com.jimmyhowe.jhdb.core.tables.columns.Column;
import com.jimmyhowe.jhdb.core.tables.columns.Columns;
import com.jimmyhowe.jhdb.core.tables.rows.Row;
import com.jimmyhowe.jhdb.core.tables.rows.Rows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility for Dumping Various Objects to the Console
 */
public class ConsoleDumper
{
    /**
     * @param resultSet Dumps a ResultSet to the Console
     */
    public static void dumpResultSet(@Nullable ResultSet resultSet)
    {
        if ( resultSet == null )
        {
            System.out.println("ConsoleDumper: Null ResultSet");

            return;
        }

        try
        {
            int columnsNumber = resultSet.getMetaData().getColumnCount();
            while ( resultSet.next() )
            {
                for ( int i = 1; i <= columnsNumber; i++ )
                {
                    if ( i > 1 ) System.out.print("\t");
                    String columnValue = resultSet.getString(i);
                    System.out.printf("%s: %s", resultSet.getMetaData().getColumnName(i), columnValue);
                }
                System.out.println("");
            }
        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Dumps the Query Log to the Console
     */
    public static void dumpQueryLog(@NotNull QueryLog queryLog)
    {
        queryLog.toConsole();
    }


    public static void dumpRows(@NotNull Rows rows)
    {
        if ( rows.isEmpty() )
        {
            System.out.println("Rows are empty!");
        }

        for ( Row row : rows.data() )
        {
            System.out.println("ROW");
            dumpRow(row);
        }
    }

    public static void dumpRow(@NotNull Row row)
    {
        if ( row.isEmpty() )
        {
            System.out.println("Row is empty!");
        }

        for ( Column column : row.data() )
        {
            System.out.println(column);
        }
    }

    public static void dumpColumns(@NotNull Columns columns)
    {
        for ( Column column : columns.data() )
        {
            System.out.println(column);
        }
    }
}
