package me.ericjiang.frontiersmen.library.lobby;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.StateEvent;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.player.ClientConnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerConnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerDisconnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.pregame.LeaveSeatEvent;
import me.ericjiang.frontiersmen.library.pregame.Pregame;
import me.ericjiang.frontiersmen.library.pregame.StartGameEvent;
import me.ericjiang.frontiersmen.library.pregame.TakeSeatEvent;
import me.ericjiang.frontiersmen.library.pregame.TransitionToGameEvent;

@Slf4j
public class Lobby extends MultiplayerModule {

    private final GameFactory gameFactory;

    private final PlayerRepository playerRepository;

    @Getter(AccessLevel.PACKAGE)
    private final NavigableMap<String, Game> games;

    @Getter(AccessLevel.PACKAGE)
    private final NavigableMap<String, Pregame> pregames;

    public Lobby(GameFactory gameFactory, PlayerRepository playerRepository) {
        this.gameFactory = gameFactory;
        this.playerRepository = playerRepository;
        this.games = Collections.synchronizedNavigableMap(
                new TreeMap<>((id1, id2) -> Integer.parseInt(id1) - Integer.parseInt(id2)));
        this.pregames = Collections.synchronizedNavigableMap(
                new TreeMap<>((id1, id2) -> Integer.parseInt(id1) - Integer.parseInt(id2)));
        games.putAll(gameFactory.loadGames());
        games.values().forEach(g -> {
            register(g);
        });
        log.info(formatLog("Loaded %d games", games.size()));
        setEventHandlers();
    }

    public Optional<Pregame> getPregame(String gameId) {
        return Optional.ofNullable(pregames.get(gameId));
    }

    public Optional<Game> getGame(String gameId) {
        return Optional.ofNullable(games.get(gameId));
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
            final String creatorId = e.getPlayerId();
            final String name = e.getName();
            final Map<String, Object> attributes = e.getAttributes();

            Preconditions.checkNotNull(creatorId);
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(attributes);
            Preconditions.checkArgument(!name.isEmpty());
            Preconditions.checkArgument(name.length() <= 32);

            Pregame pregame = gameFactory.createPregame(e.getName(), e.getPlayerId(), e.getAttributes());
            String gameId = pregame.getGameId();
            pregame.on(StartGameEvent.class, startGameEvent -> {
                Game game = pregame.getGame();
                register(game);
                games.put(gameId, game);
                pregames.remove(gameId);
                pregame.handleEvent(new TransitionToGameEvent());
            });
            pregames.put(gameId, pregame);
            register(pregame);
            broadcastState();
        });

        on(ClientConnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            PlayerNameChangeEvent playerNameChangeEvent =
                    new PlayerNameChangeEvent(playerId, playerRepository.getDisplayName(playerId));
            transmit(playerId, playerNameChangeEvent);
        });

        on(PlayerConnectionEvent.class, e -> {
        });

        on(PlayerNameChangeEvent.class, e -> {
            final String playerId = e.getPlayerId();
            final String displayName = e.getDisplayName();

            Preconditions.checkNotNull(playerId);
            Preconditions.checkNotNull(displayName);
            Preconditions.checkArgument(!displayName.isEmpty());
            Preconditions.checkArgument(displayName.length() <= 32);

            playerRepository.setDisplayName(playerId, displayName);
            log.info(formatLog("%s (%s) changed their name to %s",
                    playerRepository.getDisplayName(playerId), playerId, displayName));
            transmit(playerId, e);
            games.values().stream()
                    .filter(g -> g.getPlayers().containsKey(playerId))
                    .forEach(g -> g.handleEvent(e));
        });
    }

    private void register(Pregame pregame) {
        pregame.on(PlayerConnectionEvent.class, e -> broadcastState());
        pregame.on(PlayerDisconnectionEvent.class, e -> broadcastState());
        pregame.on(TakeSeatEvent.class, e -> broadcastState());
        pregame.on(LeaveSeatEvent.class, e -> broadcastState());
        pregame.on(StartGameEvent.class, e -> broadcastState());
        log.info(formatLog("Added Pregame %s", pregame.getGameId()));
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
