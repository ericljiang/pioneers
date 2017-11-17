package me.ericjiang.settlers.library.lobby;

import java.util.Collection;
import java.util.Map;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.Player;
import com.google.common.collect.Maps;

public class Lobby extends  MultiplayerModule {

    private Map<String, Game> games;

    private Map<String, Player> players;

    public Lobby() {
        games = Maps.newConcurrentMap();
        players = Maps.newConcurrentMap();
        games.put("1", new Game("yeah") {

            @Override
            public void onDisconnect(String playerId) {

            }

            @Override
            public void onConnect(String playerId, Player player) {

            }
        });
    }

    @Override
    public void onConnect(String playerId, Player player) {
        players.put(playerId, player);
        player.transmit(new LobbyUpdateEvent(this));
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
