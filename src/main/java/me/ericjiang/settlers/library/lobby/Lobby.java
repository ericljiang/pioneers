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

public class Lobby extends MultiplayerModule {

    private final GameFactory gameFactory;

    private final GameDao gameDao;

    @Getter
    private final Map<String, Game> games;

    public Lobby(GameFactory gameFactory, GameDao gameDao) {
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
            Game game = gameFactory.createGame(e.getAttributes());
            add(game);
        });
    }

    /**
     * Add a new game to the lobby.
     */
    private void add(Game game) {
        add(gameDao.getNewId(), game);
    }

    /**
     * Add a previously created game to the lobby.
     */
    private void add(String gameId, Game game) {
        gameDao.register(game);
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        games.put(gameId, game);
        broadcastState();
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
