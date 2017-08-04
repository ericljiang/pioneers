package me.ericjiang.settlers.core;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.SimpleAction;

@Getter
@Slf4j
public class Game {

    private final String id;

    private String name;

    private Expansion expansion;

    @Getter(AccessLevel.NONE)
    private Map<String, Player> players;

    public Game(String id, String name, String expansion) {
        this.id = id;
        this.name = name;
        this.expansion = Expansion.valueOf(expansion);
        players = new HashMap<String, Player>(this.expansion.getMaxPlayers());
    }

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

    public int getMaxPlayers() {
        return expansion.getMaxPlayers();
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

    @Getter
    public enum Expansion {
        BASE(3, 4),
        EXTENDED(5, 6);

        private int minPlayers;
        private int maxPlayers;

        Expansion(int minPlayers, int maxPlayers) {
            this.minPlayers = minPlayers;
            this.maxPlayers = maxPlayers;
        }

        @Override
        public String toString() {
            return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        }
    }
}
