package com.celeste.database.shared.exceptions.database;

import com.celeste.database.shared.exceptions.dao.DAOException;
import org.jetbrains.annotations.NotNull;

/**
 * The class {@code FailedConnectionException} is a checked {@code Exception}
 *
 * <p> The class is called when a error happened at the database</p>
 *
 * @since 3.0
 */
public class DatabaseException extends Exception {

    public DatabaseException(@NotNull final String error) {
        super(error);
    }

    public DatabaseException(@NotNull final Throwable cause) {
        super(cause);
    }

    public DatabaseException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}