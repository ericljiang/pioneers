package me.ericjiang.settlers.library.game;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class GameFactory<G extends Game> {

    private final GameDao<G> gameDao;

    protected abstract G createGameInstance(String id, Map<String, Object> attributes);

    public Map<String, G> loadGames() {
        return gameDao.loadGames();
    }

    public G createGame(Map<String, Object> attributes) {
        G game = createGameInstance(gameDao.getNewId(), attributes);
        gameDao.register(game);
        gameDao.save(game);
        return game;
    }

}
