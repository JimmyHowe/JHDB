package com.jimmyhowe.jhdb.sqlite;

public class SQLiteMemoryPlugin extends SQLitePlugin
{
    public SQLiteMemoryPlugin()
    {
        this.database(":memory:");
    }
}
