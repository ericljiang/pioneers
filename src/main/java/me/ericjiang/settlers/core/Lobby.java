package me.ericjiang.settlers.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lobby {

    // replace with database
    private Map<String, Game> games;

    public Lobby() {
        games = new HashMap<String, Game>();
    }

    public void createGame(String name, String expansion) {
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        Game game = new Game(name, expansion);
        games.put(game.getId(), game);
    }

    public List<Game> gamesForPlayer(String userId) {
        return games.values().stream()
                .filter(game -> game.hasPlayer(userId))
                .collect(Collectors.toList());
    }

    public List<Game> openGames() {
        return games.values().stream()
                .filter(game -> game.isOpen())
                .collect(Collectors.toList());
    }

    public Game getGame(String id) {
        return games.get(id);
    }
}
