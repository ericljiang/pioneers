package me.ericjiang.settlers.data;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PostgresDao {

    protected Connection connection;

    protected Statement statement;

    public PostgresDao(Connection connection) {
        this.connection = connection;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            log.error("Unable to create statement from DB connection", e);
            halt(500);
        }
    }
}
