package me.ericjiang.frontiersmen.library.player;

import java.util.Map;

import com.google.common.collect.Maps;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final Map<String, String> playerNames;

    public InMemoryPlayerRepository() {
        this.playerNames = Maps.newConcurrentMap();
    }

    @Override
    public String getDisplayName(String playerId) {
        return playerNames.get(playerId);
    }

    @Override
    public void setDisplayName(String playerId, String name) {
        playerNames.put(playerId, name);
    }

    @Override
    public boolean contains(String playerId) {
        return playerNames.containsKey(playerId);
    }

}
