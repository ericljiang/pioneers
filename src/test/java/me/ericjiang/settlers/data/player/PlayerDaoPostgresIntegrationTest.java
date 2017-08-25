package me.ericjiang.settlers.data.player;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.data.PostgresDaoIntegrationTestBase;
import me.ericjiang.settlers.data.player.PlayerDao;
import me.ericjiang.settlers.data.player.PlayerDaoPostgres;
import me.ericjiang.settlers.spark.Settlers;
import me.ericjiang.settlers.util.ShortUUID;

public class PlayerDaoPostgresIntegrationTest extends PostgresDaoIntegrationTestBase {

    private static PlayerDao playerDao;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        Connection connection = Settlers.getDatabaseConnection();
        playerDao = new PlayerDaoPostgres(connection);
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
        List<Color> colors = Lists.newArrayList(Color.BLUE, Color.BROWN, Color.GREEN);
        for (int i = 0; i < 3; i++) {
            playerDao.addPlayerToGame(gameId, players.get(i), colors.get(i), i);
        }
        Map<Color, String> retrieved = playerDao.playersForGame(gameId);
        players.forEach(playerId -> assertTrue(retrieved.containsValue(playerId)));
    }

    @Test
    public void shouldRetrievePlayersInSequenceOrder() {
        String gameId = ShortUUID.randomUUID().toString();
        List<String> players = Lists.newArrayList(
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString(),
                ShortUUID.randomUUID().toString());
        List<Color> colors = Lists.newArrayList(Color.BLUE, Color.BROWN, Color.GREEN);
        for (int i = 0; i < 3; i++) {
            playerDao.addPlayerToGame(gameId, players.get(i), colors.get(i), i);
        }
        assertEquals(Sets.newLinkedHashSet(players), Sets.newLinkedHashSet(playerDao.playersForGame(gameId).values()));
    }

    @Override
    public Collection<String> relevantTables() {
        return Sets.newHashSet("player");
    }

}
