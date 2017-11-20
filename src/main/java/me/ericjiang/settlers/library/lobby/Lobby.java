package me.ericjiang.settlers.library.lobby;

import java.util.Collection;
import java.util.Map;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.PlayerConnectionEvent;

import com.google.common.collect.Maps;

public class Lobby extends MultiplayerModule {

    private final Map<String, Game> games;

    public Lobby() {
        games = Maps.newConcurrentMap();
        setEventHandlers();
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public Collection<Game> getAllGames() {
        return games.values();
    }

    public void add(Game game) {
        games.put("1", game);
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            transmit(e.getPlayerId(), new LobbyUpdateEvent(this));
        });
    }
}
