package com.celeste.redis;

import com.celeste.redis.annotation.Channel;
import lombok.Getter;
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
    }

    public void register(final Object... objects) {
        for (Object object : objects) {
            final Channel annotation = object.getClass().getAnnotation(Channel.class);
            if (annotation == null) continue;

            registeredChannels.put(annotation.name(), annotation.value());

            new Thread(() -> provider.getConnectionInstance()
                    .subscribe((JedisPubSub) object, annotation.value())).start();

            return;
        }
    }

    public void publish(final String message, final String channel) {
        try (final Jedis jedis = provider.getConnectionInstance()) {
            jedis.publish(registeredChannels.get(channel), message);
        }
    }

    public void disconnect() {
        provider.disconnect();
    }

}
