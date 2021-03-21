package com.celeste.dao;

import com.celeste.exception.ValueNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DAO<T> {

    void createTable();

    void save(@NotNull final T... values);

    void delete(@NotNull final Object key);

    boolean contains(@NotNull final Object key);

    @NotNull
    T find(@NotNull final Object key) throws ValueNotFoundException;

    @NotNull
    List<T> findAll();

}

