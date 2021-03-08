package com.celeste.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.Properties;

public class JedisConnectionProvider implements ConnectionProvider<Jedis> {

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
    public boolean connect(Properties properties, boolean credentials) {
        try {
            if (!credentials) {
                this.jedisPool = new JedisPool(
                    properties.getProperty("hostname"),
                    Integer.parseInt(properties.getProperty("port"))
                );

                return true;
            }

            this.jedisPool = new JedisPool(
                new JedisPoolConfig(),
                properties.getProperty("hostname"),
                Protocol.DEFAULT_TIMEOUT,
                Integer.parseInt(properties.getProperty("port")),
                properties.getProperty("password"),
                false
            );

            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public void disconnect() {
        if (isRunning()) jedisPool.close();
    }

}
