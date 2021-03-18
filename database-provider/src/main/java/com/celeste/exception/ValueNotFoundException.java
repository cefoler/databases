package com.celeste.exception;

import org.jetbrains.annotations.NotNull;

public class ValueNotFoundException extends Exception {

    public ValueNotFoundException(@NotNull final String error) {
        super(error);
    }

    public ValueNotFoundException(@NotNull final Throwable cause) {
        super(cause);
    }

    public ValueNotFoundException(@NotNull final String error, @NotNull final Throwable cause) {
        super(error, cause);
    }

}
