package me.ericjiang.settlers.data.player;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.data.PostgresDao;

@Slf4j
public class PlayerDaoPostgres extends PostgresDao implements PlayerDao {

    public PlayerDaoPostgres(Connection connection) {
        super(connection);
    }

    public List<String> playersForGame(String gameId) {
        log.info("Querying database for players for game " + gameId);

        String sql = String.format(
                "SELECT player_id FROM player WHERE game_id = '%s'",
                gameId);

        List<String> players = new ArrayList<String>();
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                players.add(resultSet.getString("player_id"));
            }
        } catch (SQLException e) {
            log.error("Error getting player name: " + sql, e);
            halt(500);
        }
        return players;
    }

    public void addPlayerToGame(String gameId, String playerId) {
        log.info("Adding player " + playerId + " to game " + gameId);

        String sql = String.format(
                "INSERT INTO player VALUES ('%s', '%s')",
                gameId, playerId);

        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error adding player to game: " + sql, e);
            halt(500);
        }
    }

    public void removePlayerFromGame(String gameId, String playerId) {
        log.info("Removing player " + playerId + " from game " + gameId);

        String sql = "DELETE FROM player WHERE game_id = ? AND player_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setString(2, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error removing player from game: " + sql, e);
            halt(500);
        }
    }

    public String getName(String playerId) {
        log.info("Getting name for " + playerId);

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
        log.info(String.format("Assigning name %s to player %s", name, playerId));

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
