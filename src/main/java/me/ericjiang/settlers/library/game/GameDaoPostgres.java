package me.ericjiang.settlers.library.game;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class GameDaoPostgres<G extends Game> extends GameDao<G> {

    private final Gson gson;

    private final Class<G> gameClass;

    private final Connection connection;

    private final PreparedStatement writeGame;

    public GameDaoPostgres(Class<G> gameClass, Connection connection) {
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
    public void save(G game) {
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
    public List<G> loadGames() {
        List<String> serializedGames = readGames();
        List<G> games = deserialize(serializedGames);
        return games;
    }

    private String serialize(G game) {
        return gson.toJson(game);
    }

    private List<G> deserialize(List<String> serializedGames) {
        List<G> games = Lists.newArrayList();
        for (String s : serializedGames) {
            try {
                G game = gson.fromJson(s, gameClass);
                if (game.isPregame()) {
                    game.getPlayers().clear();
                } else {
                    game.getPlayers().values().forEach(p -> p.setOnline(false));
                }
                games.add((G) game);
            } catch (RuntimeException e) {
                String message = String.format("Failed to deserialize game as %s: %s", gameClass.getName(), s);
                log.error(message, e);
            }
        }
        return games;
    }

    private void write(int id, String serializedGame) throws SQLException {
        writeGame.setInt(1, id);
        writeGame.setString(2, serializedGame);
        writeGame.executeUpdate();
    }

    private List<String> readGames() {
        List<String> serializedGames = Lists.newArrayList();
        String sql = "select data from game";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String json = resultSet.getString(1);
                serializedGames.add(json);
            }
        } catch (SQLException e) {
            String message = "Failed to read games from PostgreSQL";
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
        return serializedGames;
    }

}
