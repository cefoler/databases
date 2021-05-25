package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.messenger.model.database.pubsub.kafka.KafkaPubSub;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class KafkaProvider implements Kafka {

  private final RemoteCredentials credentials;
  private final List<KafkaPubSub> subscribedChannels;

  private KafkaProducer<String, String> producer;
  private boolean connected;

  public KafkaProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;
    this.connected = false;
    this.subscribedChannels = new ArrayList<>();

    init();
  }

  @Override
  public void init() throws FailedConnectionException {
    try {
      final Properties settings = new Properties();
      settings.setProperty(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
          credentials.getHostname() + ":" + credentials.getPort()
      );

      settings.setProperty(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "30000");
      settings.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "30000");
      settings.setProperty(ProducerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, "100000");

      settings.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      settings.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

      this.producer = new KafkaProducer<>(settings);
      this.connected = true;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void shutdown() throws FailedShutdownException {
    try {
      producer.close();
      this.connected = false;
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    return connected;
  }

  @Override
  public MessengerType getMessengerType() {
    return MessengerType.KAFKA;
  }

  @Override
  public KafkaProducer<String, String> getProducer() {
    return producer;
  }

  @Override
  public List<KafkaPubSub> getSubscribedChannels() {
    return subscribedChannels;
  }

  @Override
  public RemoteCredentials getCredentials() {
    return credentials;
  }

}
