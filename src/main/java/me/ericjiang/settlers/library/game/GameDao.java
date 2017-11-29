package me.ericjiang.settlers.library.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GameDao<G extends Game> {

    public abstract String getNewId();

    public abstract void save(G game);

    protected abstract List<G> deserializeGames();

    public Map<String, G> loadGames() {
        List<G> games = deserializeGames();
        games.forEach(this::register);
        return games.stream().collect(Collectors.toMap(Game::getId, g -> g));
    }

    public void register(G game) {
        game.on(StateChangingEvent.class, e -> save(game));
    }

}
