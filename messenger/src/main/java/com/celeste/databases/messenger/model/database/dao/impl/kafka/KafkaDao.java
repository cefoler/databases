package com.celeste.databases.messenger.model.database.dao.impl.kafka;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.kafka.Kafka;
import com.celeste.databases.messenger.model.database.pubsub.kafka.KafkaPubSub;
import com.celeste.databases.messenger.model.entity.Listener;
import java.util.Collections;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;

public final class KafkaDao extends AbstractMessengerDao<Kafka> {

  public KafkaDao(final Kafka kafka) {
    super(kafka);
  }

  @Override
  public void publish(final String channel, final String message) {
    final ProducerRecord<String, String> record = new ProducerRecord<>(channel, message);
    final Callback callback = (data, exception) -> {
      if (exception != null) {
        try {
          throw new FailedConnectionException(exception);
        } catch (FailedConnectionException connectionException) {
          connectionException.printStackTrace();
        }
      }
    };

    getDatabase().getProducer().send(record, callback);
  }

  @Override
  public void subscribe(final String channel, final Listener listener)
      throws FailedConnectionException {
    try {
      final KafkaPubSub pubSub = new KafkaPubSub(getDatabase(), listener);
      pubSub.subscribe(Collections.singletonList(channel));
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
