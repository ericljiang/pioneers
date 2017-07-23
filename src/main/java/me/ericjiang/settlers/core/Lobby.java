package me.ericjiang.settlers.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lobby {

    private List<Game> games;

    public Lobby() {
        games = new ArrayList<Game>();
    }

    public void createGame(String name, int maxPlayers) {
        games.add(new Game(name, maxPlayers));
    }

    public List<Game> gamesForPlayer(String userId) {
        return games.stream()
                .filter(game -> game.getPlayers().contains(userId))
                .collect(Collectors.toList());
    }

    public List<Game> openGames() {
        return games.stream()
                .filter(game -> game.isOpen())
                .collect(Collectors.toList());
    }
}
