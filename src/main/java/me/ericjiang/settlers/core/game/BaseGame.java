package me.ericjiang.settlers.core.game;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseGame extends Game {

    public BaseGame(String id, String name) {
        super(id, name);
        log.info(String.format("%s game '%s' created with ID %s", getExpansion(), name, id));
    }

    @Override
    public String getExpansion() {
        return GameFactory.BASE;
    }

    @Override
    public int getMinPlayers() {
        return 3;
    }

    @Override
    public int getMaxPlayers() {
        return 4;
    }

    @Override
    protected void createBoard() {

    }
}