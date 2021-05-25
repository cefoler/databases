package com.celeste.databases.messenger.model.database.dao.impl.kafka;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.kafka.Kafka;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;

public final class KafkaDao extends AbstractMessengerDao<Kafka> {

  public KafkaDao(final Kafka messenger) {
    super(messenger);
  }

  @Override
  public void publish(final String channelName, final String message) {
    final ProducerRecord<String, String> record = new ProducerRecord<>(channelName, message);
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

  @Override @Deprecated
  public void subscribe(final String channelName, final Object instance) {
    // Kafka doesn't need to subscribe
  }

}
