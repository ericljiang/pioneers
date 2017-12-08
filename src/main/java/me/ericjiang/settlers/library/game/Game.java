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

    private final String owner;

    private final String name;

    private final Map<String, Player> players;

    private boolean pregame;

    public Game(String id, String owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.players = Maps.newConcurrentMap();
        this.pregame = true;
        setEventHandlers();
        log.info(formatLog("Game created"));
    }

    public abstract GameSummary summarize();

    public abstract Player createPlayer(String playerId);

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
    }

}
