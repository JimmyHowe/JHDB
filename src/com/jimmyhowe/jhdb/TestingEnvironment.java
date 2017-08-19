package com.jimmyhowe.jhdb;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.loggable.Log;

public class TestingEnvironment
{
    static void InitializeTestingEnvironemt()
    {
        Log.onNote(Console::white);
        Log.onInfo(Console::cyan);
        Log.onDebug(Console::yellow);
        Log.onError(message -> Console.red(message.toUpperCase()));

        DB.onCantConnect(() -> {
            Console.red("Cant Connect to Database");
            System.exit(0);
        });
    }
}
