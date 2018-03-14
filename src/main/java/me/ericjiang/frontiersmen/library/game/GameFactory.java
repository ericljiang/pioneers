package me.ericjiang.frontiersmen.library.game;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class GameFactory {

    private final GameDao gameDao;

    protected abstract Game createGameInstance(String id, String owner, Map<String, Object> attributes);

    public Map<String, Game> loadGames() {
        Map<String, Game> games = gameDao.loadGames();
        games.values().forEach(gameDao::register);
        return games;
    }

    public Game createGame(String owner, Map<String, Object> attributes) {
        Game game = createGameInstance(gameDao.getNewId(), owner, attributes);
        game.on(StartGameEvent.class, e -> gameDao.register(game));
        return game;
    }

}
