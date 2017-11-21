package me.ericjiang.settlers.library.lobby;

import java.util.Collection;
import java.util.Map;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameFactory;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.PlayerConnectionEvent;
import me.ericjiang.settlers.library.data.GameDao;

public class Lobby extends MultiplayerModule {

    private final GameFactory gameFactory;

    private final GameDao gameDao;

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

    public Collection<Game> getAllGames() {
        return games.values();
    }

    private void add(Game game) {
        games.put(gameDao.getNewId(), game);
        broadcast(new LobbyUpdateEvent(this));
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            transmit(e.getPlayerId(), new LobbyUpdateEvent(this));
        });
        on(GameCreationEvent.class, e -> {
            add(gameFactory.createGame(e.getAttributes()));
        });
    }
}
