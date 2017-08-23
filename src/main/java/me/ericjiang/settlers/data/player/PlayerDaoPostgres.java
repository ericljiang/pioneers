package me.ericjiang.settlers.data.player;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.data.PostgresDao;

@Slf4j
public class PlayerDaoPostgres extends PostgresDao implements PlayerDao {

    public PlayerDaoPostgres(Connection connection) {
        super(connection);
    }

    @Override
    public Map<Color, String> playersForGame(String gameId) {
        log.info("Querying database for players for game " + gameId);

        String sql = "SELECT player_id, color FROM player WHERE game_id = ? ORDER BY position";

        Map<Color, String> players = new LinkedHashMap<Color, String>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = Color.fromString(resultSet.getString("color"));
                String playerId = resultSet.getString("player_id");
                players.put(color, playerId);
            }
        } catch (SQLException e) {
            log.error("Error getting players: " + sql, e);
            halt(500);
        }
        return players;
    }

    @Override
    public void addPlayerToGame(String gameId, String playerId, Color color, int position) {
        log.info("Adding player " + playerId + " to game " + gameId);

        String sql = "INSERT INTO player VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setString(2, playerId);
            preparedStatement.setString(3, color.toString());
            preparedStatement.setInt(4, position);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error adding player to game: " + sql, e);
            halt(500);
        }
    }

    @Override
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

    @Override
    public String getName(String playerId) {
        log.info("Getting name for " + playerId);

        String sql = "SELECT name FROM nickname WHERE player_id = ?";

        String name = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            log.error("Error getting player name: " + sql, e);
            halt(500);
        }
        return name;
    }

    @Override
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
