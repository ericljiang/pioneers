package me.ericjiang.settlers.library.lobby;

import java.util.Map;

import lombok.Getter;
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
        this.games = gameDao.loadGames();
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
            add(gameFactory.createGame(e.getAttributes()));
        });
    }

    private void add(Game game) {
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        games.put(gameDao.getNewId(), game);
        broadcastState();
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
