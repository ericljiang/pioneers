package me.ericjiang.settlers.core;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Game {
    private String name;
    private int maxPlayers;
    private List<String> players;

    public Game(String name, int maxPlayers) {
        this.name = name;
        if (maxPlayers > 0) {
            this.maxPlayers = maxPlayers;
        } else {
            throw new IllegalArgumentException("maxPlayers must be greater than 0.");
        }
        players = new ArrayList<String>(maxPlayers);
    }

    public int currentPlayerCount() {
        return players.size();
    }

    public boolean isOpen() {
        return currentPlayerCount() < maxPlayers;
    }
}