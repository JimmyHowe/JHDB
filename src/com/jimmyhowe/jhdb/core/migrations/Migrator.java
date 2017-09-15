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

package com.jimmyhowe.jhdb.core.migrations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Migrator
 */
public class Migrator
{
    /**
     * Migrations
     */
    @NotNull
    private static List<Migration> migrations = new ArrayList<>();

    /**
     * Add a migration to the list of migrations
     *
     * @param migration Migration
     *
     * @return True if migration was added
     */
    public static boolean add(Migration migration)
    {
        return migrations.add(migration);
    }

    /**
     * Runs the Migrations Forwards
     */
    public static void up()
    {
        migrations.forEach(Migrator::runMigrationUp);
    }

    /**
     * Runs the Migrations Backwards
     */
    public static void down()
    {
        List<Migration> cachedList = migrations;

        Collections.reverse(cachedList);

        cachedList.forEach(Migrator::runMigrationDown);
    }

    /**
     * Runs a single Migration
     *
     * @param migration Migration
     */
    public static void runMigrationUp(@NotNull Migration migration)
    {
        migration.up();
    }

    /**
     * Runs a single Migration in reverse
     *
     * @param migration Migration
     */
    public static void runMigrationDown(@NotNull Migration migration)
    {
        migration.down();
    }
}
