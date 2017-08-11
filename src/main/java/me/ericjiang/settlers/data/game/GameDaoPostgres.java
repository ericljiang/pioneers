package me.ericjiang.settlers.data.game;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.Game.Phase;
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
                Game game = GameFactory.loadGame(id, creationTime, name, this, boardDao, playerDao, expansion);
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
        Game game = GameFactory.newGame(name, this, boardDao, playerDao, expansion);
        game.initializeBoard();
        games.put(game.getId(), game);

        // record in database
        String sql = "INSERT INTO game VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, game.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(game.getCreationTime()));
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, expansion);
            preparedStatement.setString(5, Phase.SETUP.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Error occured while creating record in game table: " + sql, e);
            halt(500);
        }

        return game;
    }

    @Override
    public List<Game> gamesForPlayer(String playerId) {
        List<Game> gamesForPlayer = new ArrayList<Game>();
        String sql = "SELECT game_id FROM player WHERE player_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gameId = resultSet.getString("game_id");
                gamesForPlayer.add(games.get(gameId));
            }
        } catch (SQLException e) {
            log.error("Error occured while querying games for player: " + sql, e);
            halt(500);
        }
        return gamesForPlayer;
    }

    @Override
    public List<Game> openGames() {
        List<Game> openGames = new ArrayList<>();
        String sql = "SELECT game_id FROM game WHERE phase = ? ORDER BY creation_time DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, Phase.SETUP.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("game_id");
                openGames.add(games.get(id));
            }
        } catch (SQLException e) {
            log.error("Error while finding open games: " + sql, e);
            halt(500);
        }
        return openGames;
    }

    @Override
    public List<Game> openGamesWithoutPlayer(String playerId) {
        return openGames().stream()
                .filter(game -> !game.hasPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public Game getGame(String id) {
        return games.get(id);
    }

    public Phase getPhase(String gameId) {
        Phase phase = null;
        String sql = "SELECT phase FROM game WHERE game_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            phase = Phase.valueOf(resultSet.getString("phase"));
        } catch (SQLException e) {
            log.error("Error getting game phase: " + sql, e);
            halt(500);
        }
        return phase;
    }

    public void setPhase(String gameId, Phase phase) {
        String sql = "UPDATE game SET phase = ? WHERE game_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phase.toString());
            preparedStatement.setString(2, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            phase = Phase.valueOf(resultSet.getString("phase"));
        } catch (SQLException e) {
            log.error("Error getting game phase: " + sql, e);
            halt(500);
        }
    }
}
