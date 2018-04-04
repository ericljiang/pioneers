package me.ericjiang.frontiersmen.library.game;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GameDaoPostgres extends GameDao {

    private final Gson gson;

    private final Class<? extends Game> gameClass;

    private final Connection connection;

    private final PreparedStatement writeGame;

    public GameDaoPostgres(Class<? extends Game> gameClass, Connection connection) {
        this.gson = new Gson();
        this.gameClass = gameClass;
        this.connection = connection;

        try {
            String writeGameSql = "INSERT INTO game VALUES(?, ?::JSON) "
            + "ON CONFLICT (id) DO UPDATE SET data = EXCLUDED.data";
            writeGame = connection.prepareStatement(writeGameSql);
        } catch (SQLException e) {
            String message = "Failed to prepare statements";
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public String getNewId() {
        String sql = "SELECT nextval(pg_get_serial_sequence('game', 'id')) as new_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            int id = resultSet.getInt("new_id");
            return Integer.toString(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get next serial ID from PostgreSQL", e);
        }
    }

    @Override
    public void save(Game game) {
        String serializedGame = serialize(game);
        int id = Integer.parseInt(game.getId());
        log.info("Saving Game " + id);
        try {
            write(id, serializedGame);
        } catch (SQLException e) {
            log.error("Failed to write serialized game to PostgreSQL", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Game> loadGames() {
        Map<String, Game> games = readGames().entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()),
                                          e -> deserialize(e.getValue())));
        return games;
    }

    private String serialize(Game game) {
        return gson.toJson(game);
    }

    private Game deserialize(String serializedGame) {
        Game game = gson.fromJson(serializedGame, gameClass);
        game.getPlayers().values().forEach(p -> p.setOnline(false));
        return game;
    }

    private void write(int id, String serializedGame) throws SQLException {
        writeGame.setInt(1, id);
        writeGame.setString(2, serializedGame);
        writeGame.executeUpdate();
    }

    private Map<Integer, String> readGames() {
        Map<Integer, String> serializedGames = Maps.newHashMap();
        String sql = "SELECT id, data FROM game";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String json = resultSet.getString(2);
                serializedGames.put(id, json);
            }
        } catch (SQLException e) {
            String message = "Failed to read games from PostgreSQL";
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
        return serializedGames;
    }

}
