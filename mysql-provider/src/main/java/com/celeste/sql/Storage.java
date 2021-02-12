package com.celeste.sql;

import java.util.List;
import java.util.UUID;

public interface Storage<T> {

    boolean createTable();

    void store(T t);

    void delete(UUID id);

    List<T> getAll();

}
