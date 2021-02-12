package com.celeste.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
    public boolean connect(Properties properties) {
        try {
            this.jedisPool = new JedisPool(properties.getProperty("hostname"));
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
