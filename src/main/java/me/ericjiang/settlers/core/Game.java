package me.ericjiang.settlers.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Game {

    @Getter
    public static enum Expansion {
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

    private final String uuid;
    private String name;
    private List<String> players;
    private Expansion expansion;

    public Game(String name, String expansion) {
        uuid = UUID.randomUUID().toString();
        this.name = name;
        this.expansion = Expansion.valueOf(expansion);
        players = new ArrayList<String>(this.expansion.getMaxPlayers());
    }

    public int currentPlayerCount() {
        return players.size();
    }

    public boolean isOpen() {
        return currentPlayerCount() < expansion.getMaxPlayers();
    }

    public int getMaxPlayers() {
        return expansion.getMaxPlayers();
    }

    public static Expansion[] values() {
        return Expansion.values();
    }
}
