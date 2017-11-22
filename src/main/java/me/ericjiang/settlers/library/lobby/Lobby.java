package me.ericjiang.settlers.library.lobby;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.val;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameFactory;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.PlayerConnectionEvent;
import me.ericjiang.settlers.library.PlayerDisconnectionEvent;
import me.ericjiang.settlers.library.data.GameDao;

public class Lobby<G extends Game> extends MultiplayerModule {

    private final GameFactory<G> gameFactory;

    private final GameDao<G> gameDao;

    @Getter
    private final Map<String, Game> games;

    public Lobby(GameFactory<G> gameFactory, GameDao<G> gameDao) {
        this.gameFactory = gameFactory;
        this.gameDao = gameDao;
        this.games = Maps.newHashMap();
        for (val e : gameDao.loadGames().entrySet()) {
            add(e.getKey(), e.getValue());
        }
        setEventHandlers();
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            transmit(e.getPlayerId(), new LobbyUpdateEvent(this));
        });
        on(GameCreationEvent.class, e -> {
            G game = gameFactory.createGame(e.getAttributes());
            add(game);
        });
    }

    /**
     * Add a new game to the lobby.
     */
    private void add(G game) {
        add(gameDao.getNewId(), game);
    }

    /**
     * Add a previously created game to the lobby.
     */
    private void add(String gameId, G game) {
        gameDao.register(gameId, game);
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        games.put(gameId, game);
        broadcastState();
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
