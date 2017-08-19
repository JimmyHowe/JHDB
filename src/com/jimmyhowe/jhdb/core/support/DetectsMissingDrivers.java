package com.jimmyhowe.jhdb.core.support;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Detects Lost Connections
 */
public interface DetectsMissingDrivers
{
    /**
     * @param e SQLException
     *
     * @return True or False if caused by missing driver.
     */
    default boolean causedByMissingDriver(@NotNull SQLException e)
    {
        String[] errors = new String[] {
                "No suitable driver found",
        };

        return Arrays.stream(errors).anyMatch(e.getMessage()::contains);
    }
}
