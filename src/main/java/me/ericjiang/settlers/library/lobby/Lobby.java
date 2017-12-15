package me.ericjiang.settlers.library.lobby;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.StateEvent;
import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameFactory;
import me.ericjiang.settlers.library.game.StartGameEvent;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
public class Lobby<G extends Game> extends MultiplayerModule {

    private final GameFactory<G> gameFactory;

    @Getter
    private final SortedMap<String, Game> games;

    public Lobby(GameFactory<G> gameFactory) {
        this.gameFactory = gameFactory;
        this.games = Collections.synchronizedSortedMap(new TreeMap<>());
        gameFactory.loadGames().forEach(g -> add(g));
        log.info(formatLog("Loaded %d games", games.size()));
        setEventHandlers();
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    @Override
    protected String getIdentifier() {
        return "Lobby";
    }

    @Override
    protected StateEvent toStateEvent() {
        return new LobbyUpdateEvent(this);
    }

    @Override
    protected boolean allowConnection(String playerId) {
        return true;
    }

    private void setEventHandlers() {
        on(GameCreationEvent.class, e -> {
            add(gameFactory.createGame(e.getPlayerId(), e.getAttributes()));
        });
    }

    private void add(G game) {
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        game.on(StartGameEvent.class, e -> broadcastState());
        games.put(game.getId(), game);
        broadcastState();
        log.info(formatLog("Added Game %s", game.getId()));
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
