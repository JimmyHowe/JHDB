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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBTest
{
    @BeforeEach
    void setUp()
    {
        DB.flushPlugins();
    }

    @Test
    void it_can_accept_plugins()
    {
        Plugin mockPlugin = getMockPlugin();

        DB.use(mockPlugin);

        assertEquals(DB.getDefaultPluginKey(), DB.getDefaultPluginKey());
        assertEquals(mockPlugin, DB.getDefaultPlugin());
    }

    private Plugin getMockPlugin()
    {
        return mock(Plugin.class);
    }

    @Test
    void it_can_register_multiple_plugins()
    {
        Plugin mockPlugin = getMockPlugin();

        DB.register("first", mockPlugin);
        DB.register("second", mockPlugin);

        assertEquals("first", DB.getDefaultPluginKey());
        assertEquals(mockPlugin, DB.getPlugin("first"));
        assertEquals(mockPlugin, DB.getPlugin("second"));
    }

    @Test
    void it_can_build_a_connection_using_the_default_plugin()
    {
        Plugin mockPlugin = getMockPlugin();
        Connection mockConnection = mock(Connection.class);

        when(mockPlugin.getConnection()).thenReturn(mockConnection);

        DB.use(mockPlugin);

        assertEquals("default", DB.getDefaultPluginKey());
        assertEquals(mockConnection, DB.resolveConnectionFromPlugin("default"));
    }

//    @Test
    void testLogWorks()
    {
        DB.getRunningLog().info("info");
        DB.getRunningLog().note("note");
        DB.getRunningLog().debug("debug");
        DB.getRunningLog().error("error");

        assertEquals("info", DB.getRunningLog().getRawMessageAt(0));
        assertEquals("note", DB.getRunningLog().getRawMessageAt(1));
        assertEquals("debug", DB.getRunningLog().getRawMessageAt(2));
        assertEquals("error", DB.getRunningLog().getRawMessageAt(3));
    }
}