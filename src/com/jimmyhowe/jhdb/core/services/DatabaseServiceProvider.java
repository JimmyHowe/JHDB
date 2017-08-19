package com.jimmyhowe.jhdb.core.services;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.DB;

public class DatabaseServiceProvider implements ServiceProvider
{
    @Override
    public void boot()
    {
        DB.onCantConnect(() -> {
            Console.red("Cant Connect ot Database !!!");
            DB.getQueryLog()
              .asList()
              .forEach(System.out::println);
            System.exit(0);
        });

        DB.onJdbcConnectionCreated(() -> {
            Console.cyan("dasfdsfds");
        });
    }
}
