package com.celeste.redis;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Properties;

@Getter
public class RedisProcessor {

    public final JedisConnectionProvider provider;

    public RedisProcessor() {
        this.provider = new JedisConnectionProvider();
    }

    public void connect(Properties properties, boolean credentials) {
        provider.connect(properties, credentials);
    }

    public void setupRedisChannel(final Object object, final String channel) {
        new Thread(() -> {
            final Jedis jedis = provider.getConnectionInstance();
            jedis.subscribe((JedisPubSub) object, channel);
        }).start();
    }

    public void disconnect() {
        provider.disconnect();
    }

}
