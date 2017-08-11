package me.ericjiang.settlers.data.game;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameFactory;
import me.ericjiang.settlers.data.PostgresDao;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.player.PlayerDao;
import me.ericjiang.settlers.util.ReverseInsertionOrderHashMap;

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
        games = new ReverseInsertionOrderHashMap<String, Game>();

        // populate from posgres
        String sql = "SELECT * FROM game ORDER BY creation_time";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("game_id");
                LocalDateTime creationTime = resultSet.getTimestamp("creation_time").toLocalDateTime();
                String name = resultSet.getString("name");
                String expansion = resultSet.getString("expansion");
                Game game = GameFactory.loadGame(id, creationTime, name, expansion);
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
    public Game createGame(String name, String expansion) {
        // create new instance
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        Game game = GameFactory.newGame(name, expansion);
        game.setBoardDao(boardDao);
        game.setPlayerDao(playerDao);
        game.initializeBoard();
        games.put(game.getId(), game);

        // record in database
        String sql = "INSERT INTO game VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, game.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(game.getCreationTime()));
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, expansion);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Error occured while creating record in game table: " + sql, e);
            halt(500);
        }

        return game;
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
