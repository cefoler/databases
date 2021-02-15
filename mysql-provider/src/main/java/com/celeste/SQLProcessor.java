package com.celeste;

import com.celeste.provider.SQLConnectionProvider;
import com.celeste.util.PropertiesBuilder;
import lombok.Getter;

import java.util.concurrent.ExecutorService;

@Getter
public class SQLProcessor {

    public SQLConnectionProvider sqlConnectionProvider;

    public SQLProcessor(ExecutorService executorService) {
        this.sqlConnectionProvider = new SQLConnectionProvider(executorService);
    }

    public void connect(String host, String port, String database, String user, String password) {
        sqlConnectionProvider.connect(new PropertiesBuilder()
          .with("driver", "mysql")
          .with("hostname", host)
          .with("port", port)
          .with("database", database)
          .with("username", user)
          .with("password", password)
          .wrap());
    }

    public boolean isConnected() {
        return sqlConnectionProvider.isRunning();
    }

    public void disconnect() {
        sqlConnectionProvider.disconnect();
    }

}
