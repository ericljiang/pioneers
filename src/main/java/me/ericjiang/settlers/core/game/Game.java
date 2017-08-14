package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.ConnectAction;
import me.ericjiang.settlers.core.actions.DisconnectAction;
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
    private transient Map<String, Player> connectedPlayers;

    public Game(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao) {
        this.id = id;
        this.creationTime = creationTime;
        this.name = name;
        this.gameDao = gameDao;
        this.boardDao = boardDao;
        this.playerDao = playerDao;
        connectedPlayers = new HashMap<String, Player>(getMaxPlayers());
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
        connectedPlayers.keySet().forEach(c -> {
            player.update(new ConnectAction(c, playerDao.getName(c)));
        });
        connectedPlayers.put(playerId, player);
        broadcast(new ConnectAction(playerId, playerDao.getName(playerId)));
        return true;
    }

    public void disconnectPlayer(Player player) {
        String playerId = player.id();
        connectedPlayers.remove(playerId);
        log.info("Player " + playerId + " disconnected from game " + id);
        broadcast(new DisconnectAction(playerId, playerDao.getName(playerId)));
    }

    public int currentPlayerCount() {
        return connectedPlayers.size();
    }

    public List<String> players() {
        return playerDao.playersForGame(id);
    }

    public Set<String> connectedPlayers() {
        return connectedPlayers.keySet();
    }

    public boolean hasPlayer(String playerId) {
        return players().contains(playerId);
    }

    protected void broadcast(Action action) {
        log.info("Broadcasting action " + action.getId());
        for (Player player : connectedPlayers.values()) {
            player.update(action);
        }
    }

    private void start() {
        connectedPlayers.keySet().forEach(playerId -> playerDao.addPlayerToGame(id, playerId));
    }

    public enum Phase {
        SETUP, ROLL, TRADE, BUILD
    }
}
