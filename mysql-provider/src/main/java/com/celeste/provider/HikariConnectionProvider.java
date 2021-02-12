package com.celeste.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.util.Properties;

public class HikariConnectionProvider implements SQLConnectionProvider {

    private static final String JDBC_CONNECTION_URL = "jdbc:<driver>://<hostname>:<port>/<database>";

    private HikariDataSource dataSource;

    @Override
    public Connection getConnectionInstance() {
        try {
            return dataSource.getConnection();
        } catch (Exception exception) {
            throw new RuntimeException("The datasource is not running");
        }
    }

    @Override
    public boolean isRunning() {
        return dataSource != null && dataSource.isRunning();
    }

    @Override
    public boolean connect(Properties properties) {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(JDBC_CONNECTION_URL
          .replace("<driver>", properties.getProperty("driver"))
          .replace("<hostname>", properties.getProperty("hostname"))
          .replace("<port>", properties.getProperty("port"))
          .replace("<database>", properties.getProperty("database")));

        config.setUsername(properties.getProperty("username"));
        config.setPassword(properties.getProperty("password"));

        config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "256");

        try {
            this.dataSource = new HikariDataSource(config);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void disconnect() {
        if (isRunning()) dataSource.close();
    }

}
