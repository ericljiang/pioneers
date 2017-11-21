package me.ericjiang.settlers.simple;

import java.util.Map;

import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameFactory;
import me.ericjiang.settlers.library.data.GameDao;

public class SimpleGameFactory extends GameFactory {

    public SimpleGameFactory(GameDao gameDao) {
        super(gameDao);
    }

    @Override
    public Game createGame(Map<String, Object> attributes) {
        return new SimpleGame((String) attributes.get("name"), getGameDao());
    }

}
