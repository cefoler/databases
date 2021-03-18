package com.celeste.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.Properties;

public final class JedisConnectionProvider implements Connection<Jedis> {

    private JedisPool jedisPool;

    @Override
    public Jedis getConnectionInstance() {
        return jedisPool.getResource();
    }

    @Override
    public boolean isRunning() {
        return jedisPool != null && !jedisPool.isClosed();
    }

    @Override
    public void connect(final Properties properties, final boolean credentials) {
        if (!credentials) {
            this.jedisPool = new JedisPool(
                properties.getProperty("hostname"),
                Integer.parseInt(properties.getProperty("port"))
            );

            return;
        }

        this.jedisPool = new JedisPool(
            new JedisPoolConfig(),
            properties.getProperty("hostname"),
            Integer.parseInt(properties.getProperty("port")),
            Protocol.DEFAULT_TIMEOUT,
            properties.getProperty("password"),
            false
        );
    }

    @Override
    public void disconnect() {
        if (isRunning()) jedisPool.close();
    }

}
