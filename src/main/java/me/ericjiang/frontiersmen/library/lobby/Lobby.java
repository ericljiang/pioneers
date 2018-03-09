package me.ericjiang.frontiersmen.library.lobby;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.StateEvent;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.game.StartGameEvent;
import me.ericjiang.frontiersmen.library.player.PlayerConnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerDisconnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Slf4j
public class Lobby extends MultiplayerModule {

    private final GameFactory gameFactory;

    private final PlayerRepository playerRepository;

    @Getter(AccessLevel.PACKAGE)
    private final NavigableMap<String, Game> games;

    public Lobby(GameFactory gameFactory, PlayerRepository playerRepository) {
        this.gameFactory = gameFactory;
        this.playerRepository = playerRepository;
        this.games = Collections.synchronizedNavigableMap(
                new TreeMap<>((id1, id2) -> Integer.parseInt(id1) - Integer.parseInt(id2)));
        games.putAll(gameFactory.loadGames());
        games.values().forEach(g -> {
            register(g);
        });
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
            Game game = gameFactory.createGame(e.getPlayerId(), e.getAttributes());
            register(game);
            games.put(game.getId(), game);
            broadcastState();
        });

        on(PlayerConnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            PlayerNameChangeEvent playerNameChangeEvent = new PlayerNameChangeEvent(playerRepository.getDisplayName(playerId));
            playerNameChangeEvent.setPlayerId(playerId);
            transmit(playerId, playerNameChangeEvent);
        });

        on(PlayerNameChangeEvent.class, e -> {
            String playerId = e.getPlayerId();
            log.info(formatLog("%s (%s) changed their name to %s",
                    playerRepository.getDisplayName(playerId), playerId, e.getDisplayName()));
            playerRepository.setDisplayName(e.getPlayerId(), e.getDisplayName());
            transmit(playerId, e);
            games.values().stream()
                    .filter(g -> g.getPlayers().containsKey(playerId))
                    .forEach(g -> g.handleEvent(e));
        });
    }

    private void register(Game game) {
        game.on(PlayerConnectionEvent.class, e -> broadcastState());
        game.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        game.on(StartGameEvent.class, e -> broadcastState());
        log.info(formatLog("Added Game %s", game.getId()));
    }

    private void broadcastState() {
        broadcast(new LobbyUpdateEvent(this));
    }
}
