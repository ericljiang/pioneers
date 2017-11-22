package me.ericjiang.settlers.library.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Map;
import me.ericjiang.settlers.library.Game;

public class GameDaoPostgres extends GameDao {

    private final Class<? extends Game> gameType;

    private final Gson gson;

    private int counter;

    public GameDaoPostgres(Class<? extends Game> gameType) {
        this.gameType = gameType;
        this.gson = new Gson();
        this.counter = 0;
    }

    @Override
    public Map<String, Game> loadGames() {
        // Game game = gson.fromJson("foo", gameType);
        return Maps.newConcurrentMap();
    }

    @Override
    public String getNewId() {
        return String.valueOf(counter++);
    }

    @Override
    public void save(Game game) {
        // gson.toJson(game);
    }

}
