package com.celeste.database.provider.sql.mysql;

import com.celeste.database.provider.sql.SQL;
import com.celeste.database.type.DatabaseType;
import com.celeste.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Getter(AccessLevel.PRIVATE)
public class MySQLProvider implements SQL {

    private final Properties properties;
    private final String connectionUrl;

    private HikariDataSource hikari;

    public MySQLProvider(@NotNull final Properties properties) throws DatabaseException {
        this.properties = properties;
        this.connectionUrl = "jdbc:mysql://{hostname}:{port}/{database}";

        init();
    }

    @Override @Synchronized
    public void init() throws DatabaseException {
        try {
            final HikariConfig config = new HikariConfig();

            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(connectionUrl
              .replace("{hostname}", properties.getProperty("hostname"))
              .replace("{port}", properties.getProperty("port"))
              .replace("{database}", properties.getProperty("database"))
            );

            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));

            config.setMinimumIdle(1);
            config.setMaximumPoolSize(20);

            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);

            config.addDataSourceProperty("autoReconnect", "true");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.hikari = new HikariDataSource(config);
        } catch (Throwable throwable) {
            throw new DatabaseException(throwable);
        }
    }

    @Override @Synchronized
    public void shutdown() {
        hikari.close();
    }

    @Override
    public boolean isConnect() {
        return !hikari.isClosed();
    }

    @Override @NotNull
    public DatabaseType getType() {
        return DatabaseType.MYSQL;
    }

    @Override
    public void executeUpdate(final @NotNull String sql, @NotNull final Object... values) throws SQLException {
        try (
          final Connection connection = getConnection();
          final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            applyValues(statement, values);
            statement.executeUpdate();
        }
    }

    @Override @NotNull
    public ResultSet executeQuery(final @NotNull String sql, @NotNull final Object... values) throws SQLException {
        try (
          final Connection connection = getConnection();
          final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            applyValues(statement, values);
            return statement.executeQuery();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

}
