package me.ericjiang.frontiersmen.library.game;

import java.util.Map;

import me.ericjiang.frontiersmen.library.Event;

public abstract class GameDao {

    public abstract String getNewId();

    public abstract void save(Game game);

    public abstract Map<String, Game> loadGames();

    public void register(Game game) {
        game.on(Event.class, e -> save(game));
    }

}
