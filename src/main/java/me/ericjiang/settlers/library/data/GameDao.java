package me.ericjiang.settlers.library.data;

import java.util.Map;
import me.ericjiang.settlers.library.Game;

public abstract class GameDao<G extends Game> {

    public abstract Map<String, G> loadGames();

    public abstract String getNewId();

    public abstract void save(String gameId, G game);

    public void register(String gameId, G game) {
        // game.on(StateChangingEvent.class, e -> save(gameId, game));
    }

}
