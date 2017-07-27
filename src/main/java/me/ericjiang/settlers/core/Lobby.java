package me.ericjiang.settlers.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lobby {

    private List<Game> games;

    public Lobby() {
        games = new ArrayList<Game>();
    }

    public void createGame(String name, String expansion) {
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        games.add(new Game(name, expansion));
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
