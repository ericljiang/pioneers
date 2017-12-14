package me.ericjiang.settlers.library.game;

import java.util.List;
import me.ericjiang.settlers.library.Event;

public abstract class GameDao<G extends Game> {

    public abstract String getNewId();

    public abstract void save(G game);

    public abstract List<G> loadGames();

    public void register(G game) {
        game.on(Event.class, e -> save(game));
    }

}
