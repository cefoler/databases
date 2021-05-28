package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.messenger.model.database.pubsub.kafka.KafkaPubSub;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.Getter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public final class KafkaProvider implements Kafka {

  private final RemoteCredentials credentials;
  private KafkaProducer<String, String> producer;
  private Properties properties;

  public KafkaProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      this.properties = new Properties();

      properties.setProperty(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
          credentials.getHostname() + ":" + credentials.getPort());

      properties.setProperty(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "30000");
      properties.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "30000");
      properties.setProperty(ProducerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, "100000");

      properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
          StringSerializer.class.getName());

      properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
          StringSerializer.class.getName());

      this.producer = new KafkaProducer<>(properties);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      producer.close();
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    try {
      producer.initTransactions();
      return false;
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public KafkaProducer<String, String> getProducer() {
    return producer;
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public MessengerType getMessengerType() {
    return MessengerType.KAFKA;
  }

}
