package me.ericjiang.settlers.data.game;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameFactory;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;
import me.ericjiang.settlers.core.player.Player;
import me.ericjiang.settlers.data.PostgresDaoIntegrationTestBase;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.board.BoardDaoInMemory;
import me.ericjiang.settlers.data.player.PlayerDao;
import me.ericjiang.settlers.data.player.PlayerDaoPostgres;
import me.ericjiang.settlers.spark.Settlers;

public class GameDaoPostgresIntegrationTest extends PostgresDaoIntegrationTestBase {

    private static GameDao gameDao;

    @BeforeClass
    public static void beforeClass() throws SQLException {
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
    public void shouldRetrieveGamesWeStoreInReverseOrder() {
        gameDao.createGame("foo1", GameFactory.BASE);
        Game game2 = gameDao.createGame("foo2", GameFactory.BASE);
        List<Game> games = gameDao.openGames();
        assertEquals(game2, games.get(0));
    }

    @Test
    public void shouldCreateGameWithDaos() {
        Game game = gameDao.createGame("foo", GameFactory.BASE);
        game.connectPlayer(new Player() {
            public void update(Action action) {}
            public String id() {
                return "1";
            }
        });
    }

    @Test
    public void shouldPreserverGameOrderOnPlayerDisconnect() {
        Game olderGame = gameDao.createGame("foo1", GameFactory.BASE);
        Game newerGame = gameDao.createGame("foo2", GameFactory.BASE);
        Player player = new Player() {
            public void update(Action action) {}
            public String id() {
                return "1";
            }
        };
        olderGame.connectPlayer(player);
        olderGame.disconnectPlayer(player);
        Game mostRecent = gameDao.openGamesWithoutPlayer(player.id()).get(0);
        assertEquals(newerGame, mostRecent);
    }

    @Test
    public void shouldStorePhase() {
        Game game = gameDao.createGame("foo", GameFactory.BASE);
        gameDao.setPhase(game.getId(), Phase.TRADE);
        assertEquals(Phase.TRADE, gameDao.getPhase(game.getId()));
    }

    @Test
    public void shouldStoreActivePlayer() {
        Game game = gameDao.createGame("foo", GameFactory.BASE);
        gameDao.setActivePlayer(game.getId(), Color.BLUE);
        assertEquals(Color.BLUE, gameDao.getActivePlayer(game.getId()));
    }

    @Override
    public Collection<String> relevantTables() {
        return Sets.newHashSet("game");
    }
}
