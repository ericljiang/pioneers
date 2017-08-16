package me.ericjiang.settlers.core.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.DisconnectAction;
import me.ericjiang.settlers.core.actions.JoinAction;
import me.ericjiang.settlers.core.actions.LeaveAction;
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
    private final String id;

    @Getter
    private final LocalDateTime creationTime;

    @Getter
    private String name;

    private transient GameDao gameDao;

    private transient BoardDao boardDao;

    private transient PlayerDao playerDao;

    // consider using Multimap so players can have multiple connections
    private transient Map<String, Player> playerConnections;

    /**
     * Keeps track of player (color) slots while in setup mode. Should be
     * stored in PlayerDao when the game starts.
     * K = color, V = playerId
     */
    private transient BiMap<Color, String> playerSlots;

    public Game(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao) {
        this.id = id;
        this.creationTime = creationTime;
        this.name = name;
        this.gameDao = gameDao;
        this.boardDao = boardDao;
        this.playerDao = playerDao;
        playerConnections = new HashMap<String, Player>(getMaxPlayers());
        playerSlots = HashBiMap.create();
        log.info(String.format("%s game '%s' created with id %s", getExpansion(), name, id));
    }

    /**
     * Create new board and store in DAO. Should not be called when a game is
     * is resumed as this will overwrite any existing board data.
     */
    public abstract void initializeBoard();

    public abstract String getExpansion();

    public abstract int getMaxPlayers();

    public abstract int getMinPlayers();

	public boolean connectPlayer(Player player) {
        String playerId = player.id();
        if (gameDao.getPhase(id) != Phase.SETUP && !hasPlayer(playerId)) {
            return false;
        }
        log.info("Player " + playerId + " connected to game " + id);
        // catch player up
        playerSlots.entrySet().forEach(e -> {
            Color color = e.getKey();
            String id = e.getValue();
            player.update(new JoinAction(id, playerDao.getName(id), color));
        });
        playerConnections.put(playerId, player);
        return true;
    }

    public void disconnectPlayer(Player player) {
        String playerId = player.id();
        playerConnections.remove(playerId);
        log.info("Player " + playerId + " disconnected from game " + id);
        broadcast(new DisconnectAction(playerId, playerDao.getName(playerId)));
        if (playerSlots.containsValue(playerId)) {
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

    public List<String> players() {
        return playerDao.playersForGame(id);
    }

    public Set<String> connectedPlayers() {
        return playerConnections.keySet();
    }

    public boolean hasPlayer(String playerId) {
        return players().contains(playerId);
    }

    public void handleJoinAction(JoinAction joinAction) {
        Color color = joinAction.getColor();
        String playerId = joinAction.getPlayerId();
        log.info(playerId + " wants to join " + color);
        if (!playerSlots.containsKey(color)) {
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

    protected void broadcast(Action action) {
        log.info("Broadcasting action " + action.getId());
        for (Player player : playerConnections.values()) {
            player.update(action);
        }
    }

    private void start() {
        playerConnections.keySet().forEach(playerId -> playerDao.addPlayerToGame(id, playerId));
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
        @SerializedName("brown") BROWN
    }
}
