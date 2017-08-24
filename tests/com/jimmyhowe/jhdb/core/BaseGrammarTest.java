package com.jimmyhowe.jhdb.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseGrammarTest
{
    private BaseGrammarStub baseGrammer;

    @BeforeEach
    void setUp()
    {
        baseGrammer = new BaseGrammarStub();
    }

    @Test
    void wrapArray()
    {
        List<String> actual = Arrays.asList("one", "two", "three");
        List<String> expected = Arrays.asList("one", "two", "three");

        // TODO: Recheck this
    }

    @Test
    void wrapTable_with_expression()
    {
        // TODO
    }

    @Test
    void wrapTable()
    {
        assertEquals("\"table\"", baseGrammer.wrapTable("table"));

        baseGrammer.setTablePrefix("smelly_");

        assertEquals("\"smelly_table\"", baseGrammer.wrapTable("table"));
    }

    @Test
    void wrap()
    {
        baseGrammer.setTablePrefix("test_");

        assertEquals("\"one\"", baseGrammer.wrap("one", false));
        assertEquals("\"one\"", baseGrammer.wrap("one", true));
        assertEquals("\"one\" AS \"two\"", baseGrammer.wrap("one as two", false));
        assertEquals("\"one\" AS \"test_two\"", baseGrammer.wrap("one as two", true));
    }

    @Test
    void hasAliasedValue()
    {
        assertFalse(baseGrammer.hasAliasedValue("one and two"));
        assertTrue(baseGrammer.hasAliasedValue("one as two"));
        assertTrue(baseGrammer.hasAliasedValue("one AS two"));
    }

    @Test
    void wrap_with_expression()
    {

    }

    @Test
    void wrap_with_object()
    {
    }

    @Test
    void wrapAliasedValue()
    {

    }

    @Test
    void wrapAliasedValue1()
    {
    }

    @Test
    void wrapSegments()
    {
        List<String> list = new ArrayList<>();

        list.add("one");
        list.add("two");
        list.add("three");

        assertEquals("\"one\"", baseGrammer.wrapSegments(Collections.singletonList("one")));
        assertEquals("\"one\".\"two\"", baseGrammer.wrapSegments(Arrays.asList("one", "two")));

        baseGrammer.setTablePrefix("prefix_");

        assertEquals("\"one\"", baseGrammer.wrapSegments(Collections.singletonList("one")));
        assertEquals("\"prefix_one\".\"two\"", baseGrammer.wrapSegments(Arrays.asList("one", "two")));
    }

    @Test
    void wrapValues()
    {
        @NotNull String[] values = baseGrammer.wrapValues(1, "hello");

        assertEquals("\"1\"", values[0]);
        assertEquals("\"hello\"", values[1]);
    }

    @Test
    void wrapValue()
    {
        assertEquals("\"hello\"", baseGrammer.wrapValue("hello"));
        assertEquals("\"string \"\"with\"\" quotes\"", baseGrammer.wrapValue("string \"with\" quotes"));
    }

    @Test
    void columnize()
    {
        List<String> list = new ArrayList<>();

        list.add("one");
        list.add("two");
        list.add("three");

        assertEquals("\"one\", \"two\", \"three\"", baseGrammer.columnize(list));
    }

    @Test
    void parameterize()
    {
        List<String> list = new ArrayList<>();

        list.add("one");
        list.add("two");
        list.add("three");

        assertEquals("?, ?, ?", baseGrammer.parameterize(list));
    }

    @Test
    void parameter()
    {
    }

    @Test
    void wrap_list_of_columns()
    {
        // TODO: Check this one out again

        List<Object> list = new ArrayList<>();

        list.add("one");
        list.add("two");
        list.add("three");

//        assertEquals("", baseGrammer.wrap(list));
    }

    @Test
    void pad()
    {
        assertEquals("hello ", baseGrammer.pad(" hello "));
    }

    @Test
    void getValue()
    {
    }

    @Test
    void parameter1()
    {
    }

    @Test
    void getDateFormat()
    {
    }

    @Test
    void getTablePrefix()
    {
    }

    @Test
    void setTablePrefix()
    {
    }
}

class BaseGrammarStub extends BaseGrammar
{

}