package me.ericjiang.settlers.core.player;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

import me.ericjiang.settlers.spark.Settlers;
import me.ericjiang.settlers.util.ShortUUID;

public class PlayerDaoPostgresIntegrationTest {

    private static PlayerDao playerDao;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        playerDao = new PlayerDaoPostgres(Settlers.getDatabaseConnection());
    }

    @Test
    public void shouldRetrieveNameWeStore() {
        String id = ShortUUID.randomUUID().toString();
        String name = "foo";
        playerDao.setName(id, name);
        assertEquals(name, playerDao.getName(id));
    }

    /**
     * Should update instead of insert if row exists
     */
    @Test
    public void shouldUpsertNicknames() {
        String id = ShortUUID.randomUUID().toString();
        playerDao.setName(id, "foo");
        playerDao.setName(id, "bar");
        assertEquals("bar", playerDao.getName(id));
    }

    @Test
    public void shouldRetrievePlayersWeStore() {
        String gameId = ShortUUID.randomUUID().toString();
        List<String> players = Lists.newArrayList(
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString());
        players.forEach(playerId -> playerDao.addPlayerToGame(gameId, playerId));
        List<String> retrieved = playerDao.playersForGame(gameId);
        players.forEach(playerId -> assertTrue(retrieved.contains(playerId)));
    }

    @Test
    public void shouldRetrievePlayersInOrderStored() {
        String gameId = ShortUUID.randomUUID().toString();
        List<String> players = Lists.newArrayList(
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString());
        players.forEach(playerId -> playerDao.addPlayerToGame(gameId, playerId));
        assertEquals(players, playerDao.playersForGame(gameId));
    }

}
