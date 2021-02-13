package com.celeste;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface DAO<T, U> {

    default void replace(final T key, final U value) { replace(key, value, true); }

    void replace(final T key, final U value, final boolean async);

    default void delete(final T key) { delete(key, true); }

    void delete(final T key, final boolean async);

    default CompletableFuture<Boolean> contains(final T key) { return contains(key, true); }

    CompletableFuture<Boolean> contains(final T key, final boolean async);

    default CompletableFuture<U> select(final T key) { return select(key, true); }

    CompletableFuture<U> select(final T key, final boolean async);

    default CompletableFuture<Set<U>> selectAll() { return selectAll(true); }

    CompletableFuture<Set<U>> selectAll(final boolean async);

}