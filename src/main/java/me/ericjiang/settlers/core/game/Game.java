package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.SimpleAction;
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
        // TODO: if setup phase, playerDao.addPlayerToGame(id, player.id());
        // if game has started, check if open and if player id is valid
        if (!hasPlayer(player.id())) { // unknown player
            if (gameDao.getPhase(id) != Phase.SETUP) { // game has started
                return false;
            } else { // setup mode
                playerDao.addPlayerToGame(id, player.id());
            }
        }
        connectedPlayers.put(player.id(), player);
        log.info("Player " + player.id() + " connected to game " + id);
        return true;
    }

    public void disconnectPlayer(Player player) {
        if (gameDao.getPhase(id) == Phase.SETUP) {
            playerDao.removePlayerFromGame(id, player.id());
        }
        connectedPlayers.remove(player.id());
        log.info("Player " + player.id() + " disconnected from game " + id);
    }

    public int currentPlayerCount() {
        return connectedPlayers.size();
    }

    public List<String> players() {
        return playerDao.playersForGame(id);
    }

    public boolean hasPlayer(String playerId) {
        return players().contains(playerId);
    }

    public void processAction(SimpleAction action) {
        log.info(String.format("Received SimpleAction %s from %s with data=%s",
                action.getId(), action.getPlayerId(), action.getData()));
        broadcast(action);
    }

    protected void broadcast(Action action) {
        log.info("Broadcasting action " + action.getId());
        for (Player player : connectedPlayers.values()) {
            player.update(action);
        }
    }

    public enum Phase {
        SETUP, ROLL, TRADE, BUILD
    }
}
