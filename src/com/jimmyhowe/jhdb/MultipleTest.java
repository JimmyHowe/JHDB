package com.jimmyhowe.jhdb;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.jhdb.core.schema.Blueprint;
import com.jimmyhowe.jhdb.core.schema.Schema;
import com.jimmyhowe.jhdb.core.schema.Table;
import com.jimmyhowe.jhdb.sqlite.SQLitePlugin;
import com.jimmyhowe.support.collections.Collection;
import org.jetbrains.annotations.Nullable;

public class MultipleTest extends TestingEnvironment
{
    public static void main(String[] args)
    {
        InitializeTestingEnvironemt();

        DB.register("first", new SQLitePlugin().inMemory());
        DB.register("second", new SQLitePlugin().inMemory());

        Schema.connections("first", "second")
              .drop("users");

        Schema.connections("first", "second")
              .create("users", new Blueprint()
              {
                  @Override
                  public void build(Table table)
                  {
                      table.increments("id");
                      table.string("name");
                  }
              });

        @Nullable Collection first = DB.connection("first").table("users").get();

        Console.yellow(String.valueOf(first.count()));

        DB.getQueryLog().toConsole();
    }
}
