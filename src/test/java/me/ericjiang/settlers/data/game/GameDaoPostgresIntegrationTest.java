package me.ericjiang.settlers.data.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameFactory;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.board.BoardDaoInMemory;
import me.ericjiang.settlers.data.player.PlayerDao;
import me.ericjiang.settlers.data.player.PlayerDaoPostgres;
import me.ericjiang.settlers.spark.Settlers;

public class GameDaoPostgresIntegrationTest {

    private GameDao gameDao;

    @Before
    public void before() throws SQLException {
        Connection connection = Settlers.getDatabaseConnection();
        BoardDao boardDao = new BoardDaoInMemory();
        PlayerDao playerDao = new PlayerDaoPostgres(connection);
        gameDao = new GameDaoPostgres(connection, boardDao, playerDao);
    }

    @Test
    public void shouldRetrieveGamesWeStore() {
        String name = "foo";
        Game game = gameDao.createGame(name, GameFactory.BASE);
        List<Game> games = gameDao.openGames();
        assertTrue(games.contains(game));
    }

    @Test
    public void shouldRetrieveGamesWeStoreInOrder() {
        String name = "foo";
        Game game = gameDao.createGame(name, GameFactory.BASE);
        List<Game> games = gameDao.openGames();
        assertEquals(game, games.get(games.size() - 1));
    }
}
