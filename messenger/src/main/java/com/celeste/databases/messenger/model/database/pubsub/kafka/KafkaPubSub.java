package com.celeste.databases.messenger.model.database.pubsub.kafka;

import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class KafkaPubSub extends KafkaConsumer<String, String> {

  private static final Properties settings;
  private final ScheduledExecutorService executorService;

  static {
    settings = new Properties();

    settings.setProperty(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "30000");
    settings.setProperty(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "30000");
    settings.setProperty(ConsumerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, "100000");
  }

  public KafkaPubSub() {
    super(settings);
    this.executorService = Executors.newSingleThreadScheduledExecutor();
  }

  /**
   * Every 1 second, the consumer receives all records from that time
   * and executes the handle.
   */
  public void init(final RemoteCredentials credentials) {
    settings.setProperty(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        credentials.getHostname() + ":" + credentials.getPort()
    );

    executorService.scheduleAtFixedRate(() -> {
      final ConsumerRecords<String, String> messages = poll(Duration.ofMillis(1000));
      messages.forEach(this::handle);
    }, 1, 1, TimeUnit.SECONDS);
  }

  public void shutdown() {
    executorService.shutdown();
    close();
  }

  abstract void handle(final ConsumerRecord<String, String> message);

}
