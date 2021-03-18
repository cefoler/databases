package com.celeste.dao.mongodb;

import com.celeste.dao.DAO;
import com.celeste.database.provider.mongodb.MongoDB;
import com.celeste.exception.DAOException;
import com.celeste.exception.ValueNotFoundException;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter(AccessLevel.PRIVATE)
public final class MongoDBDAO<T> implements DAO<T> {

    @Getter
    private final MongoDB database;

    private final Class<T> clazz;

    public MongoDBDAO(final MongoDB database, final Class<T> clazz) throws DAOException {
        try {
            this.database = database;
            this.clazz = clazz;
        } catch (Throwable throwable) {
            throw new DAOException(throwable);
        }
    }

    @Override
    public void createTable() {}

    @Override @SafeVarargs
    public final void save(@NotNull final T... values) {
        database.getDatastore().save(values);
    }

    @Override
    public void delete(final @NotNull Object key) {
        database.getDatastore()
          .find(clazz)
          .filter(Filters.and(
            Filters.eq("_id", key),
            Filters.eq("_id", key.toString())
          ))
          .delete();
    }

    @Override
    public boolean contains(final @NotNull Object key) {
        return database.getDatastore()
          .find(clazz)
          .filter(Filters.and(
            Filters.eq("_id", key),
            Filters.eq("_id", key.toString())
          ))
          .count() > 0;
    }

    @Override @NotNull
    public T find(final @NotNull Object key) throws ValueNotFoundException {
        final T argument = database.getDatastore()
          .find(clazz)
          .filter(Filters.and(
            Filters.eq("_id", key),
            Filters.eq("_id", key.toString())
          ))
          .first();

        if (argument == null) throw new ValueNotFoundException("Value not found");
        return argument;
    }

    @Override @NotNull
    public List<T> findAll() {
        return database.getDatastore()
          .find(clazz)
          .iterator()
          .toList();
    }

}
