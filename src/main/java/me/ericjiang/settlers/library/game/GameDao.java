package me.ericjiang.settlers.library.game;

import java.util.Map;

public abstract class GameDao<G extends Game> {

    public abstract Map<String, G> loadGames();

    public abstract String getNewId();

    public abstract void save(String gameId, G game);

    public void register(String gameId, G game) {
        game.on(StateChangingEvent.class, e -> save(gameId, game));
    }

}
