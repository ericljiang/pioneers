package me.ericjiang.settlers.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import me.ericjiang.settlers.spark.Settlers;
import org.junit.After;
import org.junit.Before;

public abstract class PostgresDaoIntegrationTestBase {

    @Before
    public void before() throws SQLException {
        clearTables(relevantTables());
    }

    @After
    public void after() throws SQLException {
        clearTables(relevantTables());
    }

    public static void clearTables(Collection<String> tables) throws SQLException {
        Connection connection = Settlers.getDatabaseConnection();
        Statement statement = connection.createStatement();
        for (String table : tables) {
            statement.execute("DELETE FROM " + table);
        }
    }

    public abstract Collection<String> relevantTables();

}
