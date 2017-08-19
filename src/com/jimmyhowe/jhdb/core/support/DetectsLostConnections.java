package com.jimmyhowe.jhdb.core.support;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Detects Lost Connections
 */
public interface DetectsLostConnections
{
    /**
     * @param e SQLException
     *
     * @return True or False if caused by lost connection.
     */
    default boolean causedByLostConnections(@NotNull SQLException e)
    {
        String[] errors = new String[] {
                "Communications link failure",
        };

        return Arrays.stream(errors).anyMatch(e.getMessage()::contains);
    }
}
