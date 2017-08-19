package com.jimmyhowe.jhdb.sqlite;

public class SQLiteMemoryAdapter extends SQLiteAdapter
{
    public SQLiteMemoryAdapter()
    {
        this.database(":memory:");
    }
}
