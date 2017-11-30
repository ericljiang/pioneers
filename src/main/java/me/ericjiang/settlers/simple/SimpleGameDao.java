package me.ericjiang.settlers.simple;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.game.GameDao;

@Slf4j
public class SimpleGameDao extends GameDao<SimpleGame> {

    private int counter = 0;

    @Override
    public String getNewId() {
        return String.valueOf(counter++);
    }

    @Override
    public void save(SimpleGame game) {
        log.info("Saving game");
    }

    @Override
    protected List<SimpleGame> deserializeGames() {
        return Lists.newArrayList();
    }

}
