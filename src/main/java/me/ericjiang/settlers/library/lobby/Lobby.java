package me.ericjiang.settlers.library.lobby;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameFactory;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
public class Lobby<G extends Game> extends MultiplayerModule {

    private final GameFactory<G> gameFactory;

    @Getter
    private final Map<String, Game> games;

    public Lobby(GameFactory<G> gameFactory) {
        this.gameFactory = gameFactory;
        this.games = Maps.newHashMap();
        gameFactory.loadGames().values().forEach(g -> add(g));
        log.info("Loaded " + games.size() + " games to the Lobby");
        setEventHandlers();
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            transmit(e.getPlayerId(), new LobbyUpdateEvent(this));
            log.info("Player " + e.getPlayerId() + " connected to the Lobby");
        });
        on(GameCreationEvent.class, e -> {
            add(gameFactory.createGame(e.getAttributes()));
        });
    }

    private void add(G game) {
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        games.put(game.getId(), game);
        broadcastState();
        log.info("Added Game " + game.getId() + " to the Lobby");
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
