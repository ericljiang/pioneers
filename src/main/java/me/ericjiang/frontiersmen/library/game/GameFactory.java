package me.ericjiang.frontiersmen.library.game;

import java.util.Map;

import lombok.AllArgsConstructor;
import me.ericjiang.frontiersmen.library.pregame.Pregame;
import me.ericjiang.frontiersmen.library.pregame.TransitionToGameEvent;

@AllArgsConstructor
public abstract class GameFactory {

    private final GameDao gameDao;

    protected abstract Game createGameInstance(String name, String gameId, String creatorId,
            Map<String, Object> attributes);

    public Map<String, Game> loadGames() {
        Map<String, Game> games = gameDao.loadGames();
        games.values().forEach(gameDao::register);
        return games;
    }

    public Pregame createPregame(String name, String creatorId, Map<String, Object> attributes) {
        String gameId = gameDao.getNewId();
        Game game = createGameInstance(name, gameId, creatorId, attributes);
        game.on(TransitionToGameEvent.class, e -> gameDao.register(game));
        return new Pregame(game, attributes);
    }

}
