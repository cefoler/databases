package com.celeste.database.messenger.model.database.provider.rabbitmq;

import com.celeste.database.messenger.model.database.type.MessengerType;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.shared.model.type.ConnectionType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;

public class RabbitMQProvider implements RabbitMQ {

  @Getter(AccessLevel.PRIVATE)
  private final Properties properties;
  private final ConnectionType connectionType;

  private Connection connection;

  public RabbitMQProvider(@NotNull final Properties properties, final ConnectionType connectionType) throws FailedConnectionException {
    this.properties = properties;
    this.connectionType = connectionType;

    init();
  }

  @Override
  public void init() throws FailedConnectionException {
    try {
      Class.forName("redis.clients.jedis.JedisPoolConfig");

      final ConnectionFactory factory = new ConnectionFactory();

      factory.setHost(properties.getProperty("hostname"));
      factory.setPort(Integer.parseInt(properties.getProperty("port")));
      factory.setVirtualHost("/");
      factory.setUsername(properties.getProperty("username"));
      factory.setPassword(properties.getProperty("password"));

      factory.setAutomaticRecoveryEnabled(true);
      factory.setTopologyRecoveryEnabled(false);
      factory.setNetworkRecoveryInterval(30);

      this.connection = factory.newConnection(Executors.newFixedThreadPool(20));
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override @SneakyThrows
  public void shutdown() {
    connection.close();
  }

  @Override
  public boolean isClosed() {
    return !connection.isOpen();
  }

  @Override @NotNull
  public MessengerType getCacheType() {
    return MessengerType.RABBITMQ;
  }

  @Override @NotNull
  public ConnectionType getConnectionType() {
    return connectionType;
  }

  @Override @NotNull
  public Channel getConnection() throws FailedConnectionException {
    try {
      if (connection == null) throw new FailedConnectionException("Connection has been closed");
      return connection.createChannel();
    } catch (IOException exception) {
      throw new FailedConnectionException(exception);
    }
  }

}