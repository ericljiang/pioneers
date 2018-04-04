package me.ericjiang.frontiersmen.simple;

import java.util.Map;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameFactory;

public class SimpleGameFactory extends GameFactory {

    public SimpleGameFactory(GameDao gameDao) {
        super(gameDao);
    }

    @Override
    protected Game createGameInstance(String name, String gameId, String creatorId, Map<String, Object> attributes) {
        return new SimpleGame(name, gameId, creatorId);
    }

}
