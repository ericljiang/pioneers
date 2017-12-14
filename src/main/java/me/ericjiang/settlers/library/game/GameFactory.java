package me.ericjiang.settlers.library.game;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class GameFactory<G extends Game> {

    private final GameDao<G> gameDao;

    protected abstract G createGameInstance(String id, String owner, Map<String, Object> attributes);

    public List<G> loadGames() {
        List<G> games = gameDao.loadGames();
        games.forEach(gameDao::register);
        return games;
    }

    public G createGame(String owner, Map<String, Object> attributes) {
        G game = createGameInstance(gameDao.getNewId(), owner, attributes);
        gameDao.register(game);
        gameDao.save(game);
        return game;
    }

}
