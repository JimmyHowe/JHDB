package com.jimmyhowe.jhdb.core.support;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ReloadsDrivers
{
    default boolean reloadDriver(@NotNull Plugin plugin)
    {
        try
        {
            Class.forName(plugin.getDriver());

            return true;
        } catch ( ClassNotFoundException e )
        {
            Console.redBackground("Cant Reload Driver");
        }

        return false;
    }
}
