package me.ericjiang.settlers.core.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Iterators;
import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.DisconnectAction;
import me.ericjiang.settlers.core.actions.GameUpdate;
import me.ericjiang.settlers.core.actions.JoinAction;
import me.ericjiang.settlers.core.actions.LeaveAction;
import me.ericjiang.settlers.core.actions.StartAction;
import me.ericjiang.settlers.core.player.Player;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.player.PlayerDao;

@Slf4j
public abstract class Game {

    /**
     * Base-64 encoded UUID without padding
     */
    @Getter
    protected final String id;

    @Getter
    private final LocalDateTime creationTime;

    @Getter
    private String name;

    /**
     * Keeps track of player (color) slots. Becomes immutable and is stored in
     * the PlayerDao when the game starts.
     * K = color, V = playerId
     */
    private transient BiMap<Color, String> playerSlots;

    /**
     * Sequence of play. Determined on game start.
     */
    private transient Iterator<Color> playerSequence;

    /**
     * Maps playerIds to their connections
     * K = playerId, V = connection
     *
     * TODO: Consider using Multimap so players can have multiple connections
     */
    private final transient Map<String, Player> playerConnections;

    private transient GameDao gameDao;

    protected transient BoardDao boardDao;

    private transient PlayerDao playerDao;

    protected Game(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao, boolean newGame) {
        this.id = id;
        this.creationTime = creationTime;
        this.name = name;
        this.gameDao = gameDao;
        this.boardDao = boardDao;
        this.playerDao = playerDao;
        playerConnections = new HashMap<String, Player>(getMaxPlayers());
        if (newGame) {
            playerSlots = HashBiMap.create();
            playerSequence = null;
            initializeBoard();
        } else {
            if (gameDao.getPhase(id) == Phase.SETUP) {
                playerSlots = HashBiMap.create();
                playerSequence = null;
            } else {
                Map<Color, String> players = playerDao.playersForGame(id);
                playerSlots = ImmutableBiMap.copyOf(players);
                playerSequence = Iterators.cycle(players.keySet());
                while (playerSequence.next() != gameDao.getActivePlayer(id)) {
                    // advance iterator
                }
            }
        }
        log.info(String.format("%s game '%s' created with id %s", getExpansion(), name, id));
    }

    public abstract String getExpansion();

    public abstract int getMaxPlayers();

    public abstract int getMinPlayers();

    /**
     * Create new tiles, edges, and intersections and store in BoardDao. Should
     * not be called when a game is is resumed as this will overwrite the
     * existing board.
     */
    protected abstract void initializeBoard();

	public boolean connectPlayer(Player player) {
        String playerId = player.id();
        Phase phase = gameDao.getPhase(id);
        if (phase != Phase.SETUP && !hasPlayer(playerId)) {
            return false;
        }
        log.info("Player " + playerId + " connected to game " + id);
        playerConnections.put(playerId, player);
        briefPlayer(player);
        return true;
    }

    public void disconnectPlayer(Player player) {
        String playerId = player.id();
        playerConnections.remove(playerId);
        log.info("Player " + playerId + " disconnected from game " + id);
        broadcast(new DisconnectAction(playerId, playerDao.getName(playerId)));
        if (gameDao.getPhase(id) == Phase.SETUP && playerSlots.containsValue(playerId)) {
            Color color = playerSlots.inverse().remove(playerId);
            broadcast(new LeaveAction(playerId, playerDao.getName(playerId), color));
        }
    }

    public int currentPlayerCount() {
        if (gameDao.getPhase(id) == Phase.SETUP) {
            return playerSlots.size();
        } else {
            return playerConnections.keySet().size();
        }
    }

    public Collection<String> players() {
        return playerDao.playersForGame(id).values();
    }

    public Set<String> connectedPlayers() {
        return playerConnections.keySet();
    }

    public boolean hasPlayer(String playerId) {
        return players().contains(playerId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action handlers

    public void handleJoinAction(JoinAction joinAction) {
        Color color = joinAction.getColor();
        String playerId = joinAction.getPlayerId();
        log.info(playerId + " wants to join " + color);
        if (!playerSlots.containsKey(color) && connectedPlayers().size() < getMaxPlayers()) {
            playerSlots.put(color, playerId);
            broadcast(joinAction);
        }
    }

    public void handleLeaveAction(LeaveAction leaveAction) {
        Color color = leaveAction.getColor();
        String playerId = leaveAction.getPlayerId();
        log.info(playerId + " wants to leave " + color);
        if (playerId.equals(playerSlots.get(color))) {
            playerSlots.remove(leaveAction.getColor());
            broadcast(leaveAction);
        }
    }

    public void handleStartAction(StartAction startAction) {
        // TODO: validate phase, owner
        start();
        broadcast(startAction);
    }

    // End of Action handlers
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void broadcast(Action action) {
        log.info("Broadcasting action " + action.getId());
        for (Player player : playerConnections.values()) {
            player.update(action);
        }
    }

    /**
     * Bring a player up to speed
     */
    private void briefPlayer(Player player) {
        player.update(new GameUpdate(getExpansion(),
                getMinPlayers(),
                getMaxPlayers(),
                boardDao.getTiles(id),
                gameDao.getPhase(id),
                gameDao.getActivePlayer(id)));
        // tell player who's connected
        playerSlots.entrySet().forEach(e -> {
            Color color = e.getKey();
            String id = e.getValue();
            String name = playerDao.getName(id);
            player.update(new JoinAction(id, name, color));
        });
    }

    private void start() {
        // add players to game and assign position
        playerSlots = ImmutableBiMap.copyOf(playerSlots);
        int position = 0;
        List<Color> colors = new ArrayList<Color>(playerSlots.keySet());
        Collections.shuffle(colors);
        playerSequence = Iterators.cycle(Collections.unmodifiableCollection(colors));
        for (Color color : colors) {
            String playerId = playerSlots.get(color);
            playerDao.addPlayerToGame(id, playerId, color, position);
            position++;
        }

        // exit setup phase
        gameDao.setPhase(id, Phase.ROLL);
        gameDao.setActivePlayer(id, playerSequence.next());
    }

    public enum Phase {
        SETUP, ROLL, TRADE, BUILD
    }

    public enum Color {
        @SerializedName("red") RED,
        @SerializedName("blue") BLUE,
        @SerializedName("white") WHITE,
        @SerializedName("orange") ORANGE,
        @SerializedName("green") GREEN,
        @SerializedName("brown") BROWN;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Color fromString(String string) {
            if (string == null) {
                return null;
            }
            return valueOf(string.toUpperCase());
        }
    }
}
