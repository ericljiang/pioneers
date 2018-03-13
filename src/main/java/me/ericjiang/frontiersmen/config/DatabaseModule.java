package me.ericjiang.frontiersmen.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import lombok.extern.slf4j.Slf4j;

@Module
@Slf4j
public class DatabaseModule {

    private static final String HEROKU_DATABASE_URL_VARIABLE = "JDBC_DATABASE_URL";

    private static final String DEFAULT_DATABASE_URL = "jdbc:postgresql:frontiersmen";

    @Provides @Singleton static Connection provideDatabaseConnection() {
        try {
            String databaseUrl = Optional.ofNullable(System.getenv(HEROKU_DATABASE_URL_VARIABLE))
                    .orElse(DEFAULT_DATABASE_URL);
            Connection connection = DriverManager.getConnection(databaseUrl);
            log.info("Connected to database at " + databaseUrl);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
