package me.ericjiang.settlers.core.player;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerDaoDB implements PlayerDao {

    private Connection connection;
    private Statement statement;

    public PlayerDaoDB() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl == null) {
            dbUrl = "jdbc:postgresql:settlers";
        }
        try {
            connection = DriverManager.getConnection(dbUrl);
            statement = connection.createStatement();
            log.info("Connected to database at " + dbUrl);
        } catch (SQLException e) {
            log.error("Unable to connect to database at " + dbUrl, e);
            halt(500, "Unable to connect to database.");
        }
    }

    public List<String> playersForGame(String gameId) {
        log.info("Querying database for players for game " + gameId);
        String sql = String.format(
                "SELECT player_id FROM player WHERE game_id = '%s'",
                gameId);
        List<String> players = new ArrayList<String>();
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String playerId = resultSet.getString("player_id");
                players.add(playerId);
            }
        } catch (SQLException e) {
            log.error("Error getting player name: " + sql, e);
            halt(500);
        }
        return players;
    }

    public void addPlayerToGame(String gameId, String playerId) {
        log.info("Adding player " + playerId + " to game");
        String sql = String.format(
                "INSERT INTO player VALUES ('%s', '%s')",
                gameId, playerId);
        log.info(sql);
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error adding player to game: " + sql, e);
            halt(500);
        }
    }

    public String getName(String playerId) {
        String sql = String.format(
                "SELECT name FROM nickname WHERE player_id = '%s';",
                playerId);
        String name = null;
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            name = resultSet.getString("name");
        } catch (SQLException e) {
            log.error("Error getting player name: " + sql, e);
            halt(500);
        }
        return name;
    }

    public void setName(String playerId, String name) {
        String sql = String.format(
                "INSERT INTO nickname VALUES ('%s', '%s') " +
                "ON CONFLICT (player_id) DO UPDATE SET name = '%s';",
                playerId, name, name);
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error updating player name: " + sql, e);
            halt(500);
        }
    }

}