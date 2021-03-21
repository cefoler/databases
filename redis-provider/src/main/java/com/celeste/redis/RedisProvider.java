package com.celeste.redis;

import com.celeste.redis.annotation.Channel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

@Getter
public final class RedisProvider {

    public final JedisConnectionProvider provider;
    public final HashMap<String, String> registeredChannels;

    public RedisProvider() {
        this.provider = new JedisConnectionProvider();
        this.registeredChannels = new LinkedHashMap<>();
    }

    public void connect(final Properties properties, final boolean credentials) {
        provider.connect(properties, credentials);
        if (provider.isRunning()) register();
    }

    private void register() {
        final Reflections reflections = new Reflections("com.celeste");
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Channel.class)) {
            final Channel annotation = clazz.getAnnotation(Channel.class);
            registeredChannels.put(annotation.name(), annotation.value());

            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    provider.getConnectionInstance()
                            .subscribe((JedisPubSub) clazz.newInstance(), annotation.value());
                }
            }).start();
        }
    }

    public void publish(final String message, final String channel) {
        try (Jedis jedis = provider.getConnectionInstance()) {
          if (registeredChannels.get(channel) == null) return;

          jedis.publish(registeredChannels.get(channel), message);
        }
    }

    public void disconnect() {
        provider.disconnect();
    }

}
