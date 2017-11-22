package me.ericjiang.settlers.simple;

import java.util.Map;

import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameFactory;

public class SimpleGameFactory extends GameFactory {

    @Override
    public Game createGame(Map<String, Object> attributes) {
        return new SimpleGame((String) attributes.get("name"));
    }

}
