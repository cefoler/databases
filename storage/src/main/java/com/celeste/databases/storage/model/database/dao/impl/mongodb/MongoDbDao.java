package com.celeste.databases.storage.model.database.dao.impl.mongodb;

import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
import com.celeste.databases.storage.model.database.dao.exception.DeleteException;
import com.celeste.databases.storage.model.database.dao.exception.FindException;
import com.celeste.databases.storage.model.database.dao.exception.SaveException;
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
import org.bson.Document;
import org.bson.conversions.Bson;

public final class MongoDbDao<T> extends AbstractStorageDao<MongoDb, T> {

  private final MongoCollection<Document> collection;

  public MongoDbDao(final MongoDb storage, final Class<T> entity) {
    super(storage, entity);

    final String collection = getEntity().getCollection();
    this.collection = createCollection(collection);
  }

  @SafeVarargs
  @Override
  public final void save(final T... entities) throws SaveException {
    try {
      final ReplaceOptions options = new ReplaceOptions().upsert(true);

      for (final T entity : entities) {
        final Object key = getEntity().getKey(entity);

        final Bson bson = Filters.eq(key);
        final Document document = serialize(entity);

        collection.replaceOne(bson, document, options);
      }
    } catch (Exception exception) {
      throw new SaveException(exception.getCause());
    }
  }

  @SafeVarargs
  @Override
  public final void delete(final T... entities) throws DeleteException {
    try {
      for (final T entity : entities) {
        final Object key = getEntity().getKey(entity);
        final Bson bson = Filters.eq(key);

        collection.deleteOne(bson);
      }
    } catch (Exception exception) {
      throw new DeleteException(exception.getCause());
    }
  }

  @Override
  public boolean contains(final Object key) {
    final Bson bson = Filters.eq(key);

    return collection.countDocuments(bson) > 1;
  }

  @Override
  public T find(final Object key) throws FindException {
    try {
      final Bson bson = Filters.eq(key);
      final Document document = collection.find(bson).first();

      if (document == null) {
        throw new ValueNotFoundException("Value not found.");
      }

      return deserialize(document);
    } catch (Exception exception) {
      throw new FindException(exception.getCause());
    }
  }

  @Override
  public List<T> findAll() throws FindException {
    try {
      final List<T> entities = new ArrayList<>();

      try (MongoCursor<Document> cursor = collection.find().cursor()) {
        while (cursor.hasNext()) {
          final Document document = cursor.next();
          final T entity = deserialize(document);

          entities.add(entity);
        }
      }

      return entities;
    } catch (Exception exception) {
      throw new FindException(exception.getCause());
    }
  }

  private MongoCollection<Document> createCollection(final String name) {
    final MongoDatabase database = getDatabase().getDatabase();

    for (final String collectionName : database.listCollectionNames()) {
      if (name.equalsIgnoreCase(collectionName)) {
        return database.getCollection(name);
      }
    }

    database.createCollection(name);
    return database.getCollection(name);
  }

  private Document serialize(final T entity) throws IllegalAccessException {
    final Map<String, Object> values = getEntity().getValues(entity);
    final Document document = new Document();

    document.putAll(values);
    return document;
  }

  private T deserialize(final Document document) throws ReflectiveOperationException {
    final Map<String, Field> values = getEntity().getValues();
    final T entity = getClazz().getConstructor().newInstance();

    for (final Entry<String, Field> entry : values.entrySet()) {
      final String name = entry.getKey();
      final Field field = entry.getValue();

      final Object object = document.getOrDefault(name, null);
      field.set(entity, object);
    }

    return entity;
  }

}
