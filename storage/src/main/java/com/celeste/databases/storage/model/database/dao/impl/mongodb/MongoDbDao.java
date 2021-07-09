package com.celeste.databases.storage.model.database.dao.impl.mongodb;

import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.impl.gson.GsonAdapter;
import com.celeste.databases.core.adapter.impl.jackson.JacksonAdapter;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
import com.celeste.databases.storage.model.database.provider.impl.mongodb.MongoDb;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

public final class MongoDbDao<T> extends AbstractStorageDao<MongoDb, T> {

  private final MongoCollection<Document> collection;

  public MongoDbDao(final MongoDb storage, final Class<T> clazz) throws FailedConnectionException {
    super(storage, clazz);

    this.collection = createCollection();
  }

  @Override
  @SafeVarargs
  public final void save(final T... entities) throws FailedConnectionException {
    save(Arrays.asList(entities));
  }

  @Override
  public void save(final Collection<T> entities) throws FailedConnectionException {
    try {
      final ReplaceOptions options = new ReplaceOptions().upsert(true);

      for (final T entity : entities) {
        final Object key = data.getKey(entity);

        final Bson bson = Filters.eq(key);
        final Document document = serialize(entity);

        collection.replaceOne(bson, document, options);
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @SafeVarargs
  @Override
  public final void delete(final T... entities) throws FailedConnectionException {
    try {
      for (final T entity : entities) {
        final Object key = data.getKey(entity);
        final Bson bson = Filters.eq(key);

        collection.deleteOne(bson);
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void delete(final Collection<T> entities) throws FailedConnectionException {
    for (final T entity : entities) {
      delete(entity);
    }
  }

  @Override
  public boolean contains(final Object key) throws FailedConnectionException {
    try {
      final Bson bson = Filters.eq(serializeObject(key));

      return collection.countDocuments(bson) > 0;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public T find(final Object key) throws ValueNotFoundException, FailedConnectionException {
    try {
      final Bson bson = Filters.eq(serializeObject(key));
      final Document document = collection.find(bson).first();

      if (document == null) {
        throw new ValueNotFoundException("Value not found");
      }

      return deserialize(document);
    } catch (ValueNotFoundException exception) {
      throw new ValueNotFoundException(exception);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public List<T> findAll() throws FailedConnectionException {
    try {
      final List<T> entities = new ArrayList<>();

      try (final MongoCursor<Document> cursor = collection.find().cursor()) {
        while (cursor.hasNext()) {
          final Document document = cursor.next();
          entities.add(deserialize(document));
        }
      }

      return entities;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  @SafeVarargs
  public final CompletableFuture<Void> saveAsync(final T... entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        save(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public CompletableFuture<Void> saveAsync(final Collection<T> entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        save(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    }, EXECUTOR);
  }

  @Override
  @SafeVarargs
  public final CompletableFuture<Void> deleteAsync(final T... entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        delete(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    }, EXECUTOR);
  }

  @Override
  public CompletableFuture<Void> deleteAsync(final Collection<T> entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        delete(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    }, EXECUTOR);
  }

  @Override
  public CompletableFuture<Boolean> containsAsync(final Object key) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return contains(key);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }

      return false;
    }, EXECUTOR);
  }

  @Override
  public CompletableFuture<T> findAsync(final Object key) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return find(key);
      } catch (FailedConnectionException | ValueNotFoundException exception) {
        exception.printStackTrace();
      }

      return null;
    }, EXECUTOR);
  }

  @Override
  public CompletableFuture<List<T>> findAllAsync() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return findAll();
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }

      return new ArrayList<>();
    }, EXECUTOR);
  }

  private MongoCollection<Document> createCollection() throws FailedConnectionException {
    try {
      final MongoDatabase database = storage.getDatabase();
      final String collection = data.getName();

      for (final String name : database.listCollectionNames()) {
        if (collection.equalsIgnoreCase(name)) {
          return database.getCollection(collection);
        }
      }

      database.createCollection(collection);
      return database.getCollection(collection);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @SneakyThrows
  private Document serialize(final T entity) {
    final Document document = new Document();

    data.getValues(entity).entrySet().stream()
        .peek(entry -> entry.setValue(serializeObject(entry.getValue())))
        .forEach(entry -> document.put(entry.getKey(), entry.getValue()));

    return document;
  }

  @SneakyThrows
  private T deserialize(final Document document) {
    final Map<String, Field> values = data.getValues();
    final T entity = Reflection.getDcConstructor(clazz).newInstance();

    for (final String name : document.keySet()) {
      final Field field = values.get(name);

      if (field == null) {
        continue;
      }

      final Object object = document.get(name);
      field.set(entity, deserializeObject(object, field));

      values.remove(name);
    }

    for (final Field field : values.values()) {
      field.set(entity, null);
    }

    return entity;
  }

  @Nullable
  private Object serializeObject(@Nullable final Object object) {
    if (object instanceof Object[]) {
      final Object[] objects = (Object[]) object;
      return Arrays.asList(objects);
    }

    if (object instanceof Collection<?>) {
      final Collection<?> collection = (Collection<?>) object;
      return new ArrayList<>(collection);
    }

    if (object instanceof Enum<?>) {
      final Enum<?> enumeration = (Enum<?>) object;
      return enumeration.name();
    }

    return object;
  }

  @Nullable
  @SneakyThrows
  private Object deserializeObject(@Nullable final Object object, final Field field) {
    final Class<?> clazz = field.getType();

    if (object instanceof Document) {
      final Document document = (Document) object;
      return GsonAdapter.getInstance().deserialize(document.toJson(), clazz);
    }

    if (object instanceof List<?>) {
      final Class<?> type = Class.forName(field.getGenericType().getTypeName()
          .replaceAll(".*<|>.*", "")
          .replace("[]", ""));

      final List<?> list = ((List<?>) object).stream()
          .map(subObject -> {
            if (subObject instanceof Document) {
              try {
                final Document document = (Document) subObject;
                return JacksonAdapter.getInstance().deserialize(document.toJson(), type);
              } catch (JsonDeserializeException ignored) {
              }
            }

            return subObject;
          })
          .collect(Collectors.toList());

      if (clazz.isArray()) {
        final Object[] array = (Object[]) Array.newInstance(type, list.size());
        return list.toArray(array);
      }

      if (clazz.equals(Set.class)) {
        return new LinkedHashSet<>(list);
      }

      return list;
    }

    if (clazz.isEnum()) {
      final String name = String.valueOf(object);
      return Arrays.stream(clazz.getEnumConstants())
          .filter(enumeration -> String.valueOf(enumeration).equals(name))
          .findFirst()
          .orElseThrow(() -> new JsonDeserializeException("Object isn't an invalid instance."));
    }

    return object;
  }

}
