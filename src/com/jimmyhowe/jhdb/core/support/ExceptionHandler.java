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

package com.jimmyhowe.jhdb.core.support;

import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.jhdb.core.exceptions.DatabaseAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

/**
 * Utility class for handling JDBC
 */
public class ExceptionHandler
{
    @Nullable
    public Exception handle(@NotNull SQLException e)
    {
        if ( DB.printStackTrace )
        {
            e.printStackTrace();
        }

        if ( matches(e.getMessage(), "No suitable driver found for jdbc:") )
        {
            System.out.println("smOKETHEM FICH");
        }

        if ( matches(e.getMessage(), "Can't create database '[\\w]+'; database exists") )
        {
            return new DatabaseAlreadyExistsException();
        }

        return null;
    }

    private boolean matches(@NotNull String message, @NotNull String regex)
    {
        if ( message.matches(regex) )
        {
            return true;
        }

        return false;
    }
}
