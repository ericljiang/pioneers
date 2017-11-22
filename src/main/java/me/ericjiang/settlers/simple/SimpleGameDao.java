package me.ericjiang.settlers.simple;

import java.util.Map;

import com.google.common.collect.Maps;

import me.ericjiang.settlers.library.game.GameDao;

public class SimpleGameDao extends GameDao<SimpleGame> {

    private int counter = 0;

    @Override
    public Map<String, SimpleGame> loadGames() {
        return Maps.newHashMap();
    }

    @Override
    public String getNewId() {
        return String.valueOf(counter++);
    }

    @Override
    public void save(String gameId, SimpleGame game) {

    }

}
