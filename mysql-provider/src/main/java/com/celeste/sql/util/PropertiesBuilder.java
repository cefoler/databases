package com.celeste.sql.util;

import java.util.Properties;

public class PropertiesBuilder {

    private final Properties properties;

    public PropertiesBuilder() {
        this.properties = new Properties();
    }

    public PropertiesBuilder with(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public Properties wrap() {
        return this.properties;
    }

}
