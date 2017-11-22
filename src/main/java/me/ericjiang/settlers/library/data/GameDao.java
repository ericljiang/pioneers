package me.ericjiang.settlers.library.data;

import java.util.Map;
import me.ericjiang.settlers.library.Game;

public abstract class GameDao {

    public abstract Map<String, Game> loadGames();

    public abstract String getNewId();

    public abstract void save(Game game);

    public void register(Game game) {
        // game.on(StateChangingEvent.class, e -> save(game));
    }

}
