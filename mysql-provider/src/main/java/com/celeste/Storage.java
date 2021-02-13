package com.celeste;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Storage<T> {

    Boolean createTable();

    void store(T t);

    void delete(UUID id);

    T getByValue(UUID id);

    List<T> getAll();

}
