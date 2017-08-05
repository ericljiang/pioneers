package me.ericjiang.settlers.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameFactory;

@Slf4j
public class Lobby {

    // TODO: replace with database gameDAO.getGame(id)
    private Map<String, Game> games;

    public Lobby() {
        games = new HashMap<String, Game>();
    }

    public void createGame(String name, String expansion) {
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        Game game = GameFactory.newGame(name, expansion);
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
