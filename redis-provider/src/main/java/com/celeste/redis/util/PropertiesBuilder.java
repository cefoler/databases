package com.celeste.redis.util;

import java.util.Properties;

public final class PropertiesBuilder {

    private final Properties properties;

    public PropertiesBuilder() {
        this.properties = new Properties();
    }

    public PropertiesBuilder with(final String key, final String value) {
        properties.put(key, value);
        return this;
    }

    public final Properties wrap() {
        return this.properties;
    }

}
