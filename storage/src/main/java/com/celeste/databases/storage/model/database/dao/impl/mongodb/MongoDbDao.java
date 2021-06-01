package com.celeste.databases.storage.model.database.dao.impl.mongodb;

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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;

public final class MongoDbDao<T> extends AbstractStorageDao<MongoDb, T> {

  private final MongoCollection<T> collection;

  public MongoDbDao(final MongoDb storage, final Class<T> entity) throws FailedConnectionException {
    super(storage, entity);

    this.collection = createCollection();
  }

  @SafeVarargs
  @Override
  public final void save(final T... entities) throws FailedConnectionException {
    try {
      final ReplaceOptions options = new ReplaceOptions().upsert(true);

      for (final T entity : entities) {
        final Object key = getEntity().getKey(entity);

        final Bson bson = Filters.eq(key);
        final Document document = serialize(entity);

        collection.replaceOne(bson, entity, options);
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
        final Object key = getEntity().getKey(entity);
        final Bson bson = Filters.eq(key);

        collection.deleteOne(bson);
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public boolean contains(final Object key) throws FailedConnectionException {
    try {
      final Bson bson = Filters.eq(key);

      return collection.countDocuments(bson) > 1;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public T find(final Object key) throws ValueNotFoundException, FailedConnectionException {
    try {
      final Bson bson = Filters.eq(key);
      return collection.find(bson).first();
//      final Document document = collection.find(bson).first();
//
//      if (document == null) {
//        throw new ValueNotFoundException("Value not found");
//      }
//
//      return deserialize(document);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public List<T> findAll() throws FailedConnectionException {
    try {
      final List<T> entities = new ArrayList<>();

      try (MongoCursor<T> cursor = collection.find().cursor()) {
        while (cursor.hasNext()) {
          entities.add(cursor.next());
//          final Document document = cursor.next();
//          entities.add(deserialize(document));
        }
      }

      return entities;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  private MongoCollection<T> createCollection()
      throws FailedConnectionException {
    try {
      final MongoDatabase database = getDatabase().getDatabase();
      final String collection = getEntity().getName();

      for (final String name : database.listCollectionNames()) {
        if (collection.equalsIgnoreCase(name)) {
          return database.getCollection(collection, getClazz());
        }
      }

      database.createCollection(collection);
      return database.getCollection(collection, getClazz());
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @SneakyThrows
  private Document serialize(final T entity) {
    final Map<String, Object> values = getEntity().getValues(entity);
    final Document document = new Document();

    document.putAll(values);
    return document;
  }

  @SneakyThrows
  private T deserialize(final Document document) {
    System.out.println("A");
    final Map<String, Field> values = getEntity().getValues();
    System.out.println("B");
    final T entity = Reflection.getDcConstructor(getClazz()).newInstance();
    System.out.println("C");

    for (final Entry<String, Field> entry : values.entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue());
      final Object object = document.getOrDefault(entry.getKey(), null);
      System.out.println(object);
      System.out.println("D");
      entry.getValue().set(entity, object);
      System.out.println("E");
    }

    return entity;
  }

}
