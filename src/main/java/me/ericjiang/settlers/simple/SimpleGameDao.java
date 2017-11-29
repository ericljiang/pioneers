package me.ericjiang.settlers.simple;

import java.util.List;

import com.google.common.collect.Lists;

import me.ericjiang.settlers.library.game.GameDao;

public class SimpleGameDao extends GameDao<SimpleGame> {

    private int counter = 0;

    @Override
    public String getNewId() {
        return String.valueOf(counter++);
    }

    @Override
    public void save(SimpleGame game) {
        System.out.println("Saving game");
    }

    @Override
    protected List<SimpleGame> deserializeGames() {
        return Lists.newArrayList();
    }

}
