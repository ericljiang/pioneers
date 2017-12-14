package me.ericjiang.settlers.library.game;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.StateEvent;
import me.ericjiang.settlers.library.player.Player;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
@Getter
public abstract class Game extends MultiplayerModule {

    private final String id;

    private final String name;

    private final Player creator;

    private final Map<String, Player> players;

    private boolean pregame;

    /**
     * No-args constructor for Gson
     */
    protected Game() {
        this.id = "";
        this.name = "";
        this.creator = null;
        this.players = Maps.newConcurrentMap();
        setEventHandlers();
    }

    public Game(String id, String creatorId, String name) {
        this.id = id;
        this.name = name;
        this.creator = new Player(creatorId);
        this.players = Maps.newConcurrentMap();
        this.pregame = true;
        setEventHandlers();
        log.info(formatLog("Game created"));
    }

    public abstract int minimumPlayers();

    public abstract int maximumPlayers();

    public abstract GameSummary summarize();

    protected abstract Player createPlayer(String playerId);

    @Override
    protected String getIdentifier() {
        return String.format("Game %s", id);
    }

    @Override
    protected StateEvent toStateEvent() {
        return new GameUpdateEvent(this);
    }

    @Override
    protected boolean allowConnection(String playerId) {
        if (pregame) {
            return true;
        }
        return players.containsKey(playerId);
    }

    protected void start() {
        this.pregame = false;
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            if (pregame) {
                players.put(playerId, createPlayer(playerId));
            }
            players.get(playerId).setOnline(true);
            broadcast(new GameUpdateEvent(this));
        });

        on(PlayerDisconnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            Player player = players.get(playerId);
            if (pregame) {
                players.remove(playerId);
            } else {
                player.setOnline(false);
            }
            broadcast(new GameUpdateEvent(this));
        });

        on(StartGameEvent.class, e -> {
            if (players.size() >= minimumPlayers() && players.size() <= maximumPlayers()) {
                start();
                broadcast(new GameUpdateEvent(this));
            }
        });
    }

}
