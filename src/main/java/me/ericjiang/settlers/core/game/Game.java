package me.ericjiang.settlers.core.game;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.Player;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.SimpleAction;
import me.ericjiang.settlers.util.ShortUUID;

@Getter
@Slf4j
public abstract class Game {

    /**
     * Base-64 encoded UUID without padding
     */
    private final String id;

    private String name;

    @Getter(AccessLevel.NONE)
    private Map<String, Player> players;

    public Game(String name) {
        this.id = ShortUUID.randomUUID().toString();
        this.name = name;
        players = new HashMap<String, Player>(getMaxPlayers());
    }

    public abstract String getExpansion();

    public abstract int getMaxPlayers();

    public abstract int getMinPlayers();

	public void connectPlayer(Player player) {
        // TODO: if game has started, check if open and if player id is valid
        log.info("Player " + player.id() + " connected to game " + id);
        players.put(player.id(), player);
    }
    
    public void disconnectPlayer(Player player) {
        log.info("Player " + player.id() + " disconnected from game " + id);
        players.put(player.id(), null);
    }

    public int currentPlayerCount() {
        return (int) players.values().stream()
                .filter(p -> p != null)
                .count();
    }

    public boolean isOpen() {
        return currentPlayerCount() < getMaxPlayers(); // TODO: && phase == Phase.SETUP
    }

    public boolean hasPlayer(String playerId) {
        for (String player : players.keySet()) {
            if (player == playerId) {
                return true;
            }
        }
        return false;
    }

    public void processAction(SimpleAction action) {
        log.info(String.format("Received SimpleAction %s from %s with data=%s",
                action.getId(), action.getPlayerId(), action.getData()));
        broadcast(action);
    }

    private void broadcast(Action action) {
        log.info("Broadcasting action " + action.getId());
        for (Player player : players.values()) {
            if (player != null) {
                player.update(action);
            }
        }
    }
}
