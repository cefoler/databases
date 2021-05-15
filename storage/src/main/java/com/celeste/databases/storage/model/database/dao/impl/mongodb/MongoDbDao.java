package com.celeste.databases.storage.model.database.dao.impl.mongodb;

import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
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

  private final MongoCollection<Document> collection;

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

        collection.replaceOne(bson, document, options);
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
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
      throw new FailedConnectionException(exception.getCause());
    }
  }

  @Override
  public boolean contains(final Object key) throws FailedConnectionException {
    try {
      final Bson bson = Filters.eq(key);

      return collection.countDocuments(bson) > 1;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
    }
  }

  @Override
  public T find(final Object key) throws ValueNotFoundException, FailedConnectionException {
    try {
      final Bson bson = Filters.eq(key);
      final Document document = collection.find(bson).first();

      if (document == null) {
        throw new ValueNotFoundException("Value not found");
      }

      return deserialize(document);
    } catch (ValueNotFoundException exception) {
      throw new ValueNotFoundException(exception.getCause());
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
    }
  }

  @Override
  public List<T> findAll() throws FailedConnectionException {
    try {
      final List<T> entities = new ArrayList<>();

      try (MongoCursor<Document> cursor = collection.find().cursor()) {
        while (cursor.hasNext()) {
          final Document document = cursor.next();
          entities.add(deserialize(document));
        }
      }

      return entities;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
    }
  }

  private MongoCollection<Document> createCollection()
      throws FailedConnectionException {
    try {
      final MongoDatabase database = getDatabase().getDatabase();
      final String collection = getEntity().getName();

      for (final String name : database.listCollectionNames()) {
        if (collection.equalsIgnoreCase(name)) {
          return database.getCollection(collection);
        }
      }

      database.createCollection(collection);
      return database.getCollection(collection);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
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
    final Map<String, Field> values = getEntity().getValues();
    final T entity = getClazz().getConstructor().newInstance();

    for (final Entry<String, Field> entry : values.entrySet()) {
      final Object object = document.getOrDefault(entry.getKey(), null);
      entry.getValue().set(entity, object);
    }

    return entity;
  }

}
