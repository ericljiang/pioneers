package me.ericjiang.settlers.core.game;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.PostgresDao;
import me.ericjiang.settlers.core.board.BoardDao;
import me.ericjiang.settlers.core.player.PlayerDao;

/**
 * Loads games from postgres and keeps them stored in memory. Every new game is
 * saved in postgres upon creation.
 */
@Slf4j
public class GameDaoPostgres extends PostgresDao implements GameDao {

    private BoardDao boardDao;

    private PlayerDao playerDao;

    private Map<String, Game> games;

    public GameDaoPostgres(Connection connection, BoardDao boardDao, PlayerDao playerDao) {
        super(connection);
        this.boardDao = boardDao;
        this.playerDao = playerDao;
        games = new HashMap<String, Game>();

        // populate from posgres
        String sql = String.format("SELECT * FROM game");

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("game_id");
                String name = resultSet.getString("name");
                String expansion = resultSet.getString("expansion");
                Game game = GameFactory.loadGame(id, name, expansion);
                game.setBoardDao(boardDao);
                game.setPlayerDao(playerDao);
                games.put(id, game);
            }
        } catch (SQLException e) {
            log.error("Error occured while creating record in game table: " + sql, e);
            halt(500);
        }
    }

    @Override
    public void createGame(String name, String expansion) {
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        Game game = GameFactory.newGame(name, expansion);
        game.setBoardDao(boardDao);
        game.setPlayerDao(playerDao);
        game.initializeBoard();
        games.put(game.getId(), game);

        String sql = String.format(
                "INSERT INTO game VALUES ('%s', '%s', '%s')",
                game.getId(), expansion, name);

        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error occured while creating record in game table: " + sql, e);
            halt(500);
        }
    }

    @Override
    public List<Game> gamesForPlayer(String playerId) {
        return games.values().stream()
                .filter(game -> game.hasPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Game> openGames() {
        return games.values().stream()
                .filter(game -> game.isOpen())
                .collect(Collectors.toList());
    }

    @Override
    public List<Game> openGamesWithoutPlayer(String playerId) {
        return games.values().stream()
                .filter(game -> game.isOpen() && !game.hasPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public Game getGame(String id) {
        return games.get(id);
    }
}
