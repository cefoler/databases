package com.celeste.redis;

import com.celeste.redis.util.PropertiesBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Getter
@AllArgsConstructor
public class RedisProcessor {

    public final JedisConnectionProvider jedisConnectionProvider;

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
