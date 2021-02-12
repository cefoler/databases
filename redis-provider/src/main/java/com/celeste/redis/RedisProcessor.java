package com.celeste.redis;

import com.celeste.redis.util.PropertiesBuilder;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Getter
public class RedisProcessor {

    public final JedisConnectionProvider jedisConnectionProvider;

    public RedisProcessor() {
        this.jedisConnectionProvider = new JedisConnectionProvider();
    }

    public void connect(String hostname) {
        jedisConnectionProvider.connect(new PropertiesBuilder()
            .with("hostname", hostname)
            .wrap()
        );
    }

    public void setupRedisChannel(final Object object, final String channel) {
        jedisConnectionProvider.connect(null);

        new Thread(() -> {
            try (
              final Jedis jedis = jedisConnectionProvider.getConnectionInstance()
            ) {
                jedis.subscribe((JedisPubSub) object, channel);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();
    }

}
