package me.ericjiang.settlers;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public class Lobby extends  MultiplayerModule {

    private Map<String, Game> games;

    private Map<String, Player> players;

    public Lobby() {
        games = Maps.newConcurrentMap();
        players = Maps.newConcurrentMap();
    }

    @Override
    public void onConnect(String playerId, Player player) {
        players.put(playerId, player);
        player.transmit(new Event());
    }

    @Override
    public void onDisconnect(String playerId) {
        players.remove(playerId);
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public Collection<Game> getAllGames() {
        return games.values();
    }
}
