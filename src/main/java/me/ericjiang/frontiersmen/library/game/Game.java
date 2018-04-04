package me.ericjiang.frontiersmen.library.game;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.StateEvent;
import me.ericjiang.frontiersmen.library.player.Player;
import me.ericjiang.frontiersmen.library.player.PlayerConnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerDisconnectionEvent;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.pregame.TransitionToGameEvent;

@Slf4j
public abstract class Game extends MultiplayerModule {

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private final Player creator;

    /**
     * Maps Player IDs to Player object representations
     */
    @Getter
    private final Map<String, Player> players;

    /**
     * Ordered list of "seats"
     */
    private transient Iterator<Player> playerOrder;

    @Getter
    private transient Player currentPlayer;

    /**
     * No-args constructor for Gson to ensure event handlers are set.
     */
    protected Game() {
        this.id = "";
        this.name = "";
        this.creator = null;
        this.players = Maps.newConcurrentMap();
        setEventHandlers();
    }

    public Game(String name, String id, String creatorId) {
        this.name = name;
        this.id = id;
        this.creator = new Player(creatorId);
        this.players = Maps.newConcurrentMap();
        setEventHandlers();
        log.info(formatLog("Game created"));
    }

    public abstract GameSummary summarize();

    public abstract int minimumPlayers();

    public abstract int maximumPlayers();

    protected abstract Player createPlayer(String playerId, int seat);

    public void setPlayers(Player[] playerSeats) {
        for (int seat = 0; seat < playerSeats.length; seat++) {
            if (playerSeats[seat] != null) {
                String playerId = playerSeats[seat].getId();
                Player player = createPlayer(playerId, seat);
                players.put(playerId, player);
            }
        }
        this.playerOrder = Iterators.unmodifiableIterator(Iterators.cycle(
                players.values().stream()
                        .sorted((p1, p2) -> Integer.compare(p1.getSeat(), p2.getSeat()))
                        .collect(Collectors.toList())));
        this.currentPlayer = playerOrder.next();
    }

    public void endTurn() {
        currentPlayer = playerOrder.next();
    }

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
        return players.containsKey(playerId);
    }

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            players.get(playerId).setOnline(true);
            broadcast(new GameUpdateEvent(this));
        });

        on(PlayerDisconnectionEvent.class, e -> {
            String playerId = e.getPlayerId();
            Player player = players.get(playerId);
            player.setOnline(false);
            broadcast(new GameUpdateEvent(this));
        });

        on(PlayerNameChangeEvent.class, e -> {
            broadcast(toStateEvent());
        });

        on(TransitionToGameEvent.class, e -> {

        });
    }

}
