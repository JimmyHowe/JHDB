package com.jimmyhowe.jhdb.core.support;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.Adapter;
import org.jetbrains.annotations.NotNull;

public interface ReloadsDrivers
{
    default boolean reloadDriver(@NotNull Adapter adapter)
    {
        try
        {
            Class.forName(adapter.getDriver());

            return true;
        } catch ( ClassNotFoundException e )
        {
            Console.redBackground("Cant Reload Driver");
        }

        return false;
    }
}
