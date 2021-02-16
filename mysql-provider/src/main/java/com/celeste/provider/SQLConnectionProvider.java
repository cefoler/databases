package com.celeste.provider;

import com.celeste.function.SqlFunction;
import com.celeste.mapper.SqlEntryMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@RequiredArgsConstructor
public class SQLConnectionProvider implements ConnectionProvider<Connection> {

    private static final String JDBC_CONNECTION_URL = "jdbc:<driver>://<hostname>:<port>/<database>";

    private final ExecutorService executorService;
    private HikariDataSource dataSource;

    public CompletableFuture<Boolean> execute(String sql, Object... values) {
        return CompletableFuture.supplyAsync(() -> {

            try (
              Connection connection = getConnectionInstance();
              PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                applyValuesToStatement(statement, values);

                return statement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }, getExecutorService());
    }

    public CompletableFuture<Boolean> executeUpdate(String sql, Object... statementValues) {
        return CompletableFuture.supplyAsync(() -> {

            try (
              Connection connection = getConnectionInstance();
              PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                this.applyValuesToStatement(statement, statementValues);

                statement.executeUpdate();
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return false;
        }, getExecutorService());
    }

    public CompletableFuture<ResultSet> executeQuery(String sql, Object... statementValues) {
        return CompletableFuture.supplyAsync(() -> {
            try (
              Connection connection = getConnectionInstance();
              PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                applyValuesToStatement(statement, statementValues);

                return statement.executeQuery();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return null;
        }, getExecutorService());
    }

    public <T> CompletableFuture<List<T>> selectAsList(String sql, SqlFunction<ResultSet, T> function, Object... statementValues) {
        return CompletableFuture.supplyAsync(() -> {
            final List<T> collected = Collections.synchronizedList(new ArrayList<>());

            try (
              Connection connection = getConnectionInstance();
              PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                applyValuesToStatement(statement, statementValues);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final T obj = function.apply(resultSet);

                        if (obj != null) collected.add(obj);
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return collected;
        }, getExecutorService());
    }

    public <T> CompletableFuture<T> getFirstFromQuery(String sql, SqlFunction<ResultSet, T> function, Object... statementValues) {
        return CompletableFuture.supplyAsync(() -> {

            try (
              Connection connection = getConnectionInstance();
              PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                applyValuesToStatement(statement, statementValues);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.first()) {
                        return function.apply(resultSet);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return null;
        }, getExecutorService());
    }

    public <K, V> CompletableFuture<Map<K, V>> mapFromQuery(String sql, SqlEntryMapper<K, V> mapper, Object... statementValues) {
        return CompletableFuture.supplyAsync(() -> {
            final Map<K, V> map = new ConcurrentHashMap<>();

            try (ResultSet resultSet = (ResultSet) executeQuery(sql, statementValues)) {
                while (resultSet.next()) {
                    final K key = mapper.transformKey(resultSet);
                    final V value = mapper.transformValue(resultSet);

                    map.put(key, value);
                }
                return map;
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return Collections.emptyMap();
        }, getExecutorService());
    }

    public void applyValuesToStatement(PreparedStatement statement, Object... values) {
        try {
            if (values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject(i + 1, values[i]);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override @SneakyThrows
    public Connection getConnectionInstance() {
        return dataSource.getConnection();
    }

    @Override
    public boolean isRunning() {
        return dataSource.isRunning();
    }

    @Override
    public void connect(Properties properties) {
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(JDBC_CONNECTION_URL
          .replace("<driver>", properties.getProperty("driver"))
          .replace("<hostname>", properties.getProperty("hostname"))
          .replace("<port>", properties.getProperty("port"))
          .replace("<database>", properties.getProperty("database")));

        config.setUsername(properties.getProperty("username"));
        config.setPassword(properties.getProperty("password"));

        config.setMinimumIdle(1);
        config.setMaximumPoolSize(40);

        config.setConnectionTimeout(120000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void disconnect() {
        if (isRunning()) dataSource.close();
    }

}
