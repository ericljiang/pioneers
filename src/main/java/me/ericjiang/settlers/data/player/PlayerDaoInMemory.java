package me.ericjiang.settlers.data.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.data.player.PlayerDao;

@Slf4j
public class PlayerDaoInMemory implements PlayerDao {

    private ListMultimap<String, String> playersForGame;

    private Map<String, String> nickNames;

    public PlayerDaoInMemory() {
        playersForGame = ArrayListMultimap.create();
        nickNames = new HashMap<String, String>();
    }

    public List<String> playersForGame(String gameId) {
        return playersForGame.get(gameId);
    }

    public void addPlayerToGame(String gameId, String playerId) {
        playersForGame.put(gameId, playerId);
        log.info(String.format("Added player %s to game %s", playerId, gameId));
    }

    public String getName(String playerId) {
        return nickNames.get(playerId);
    }

    public void setName(String playerId, String name) {
        nickNames.put(playerId, name);
        log.info(String.format("Set player %s's name to %s", playerId, name));
    }
}
