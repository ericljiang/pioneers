package me.ericjiang.frontiersmen.tictactoe;

import java.util.Map;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameFactory;

public class TicTacToeGameFactory extends GameFactory {

    public TicTacToeGameFactory(GameDao gameDao) {
        super(gameDao);
    }

    @Override
    protected Game createGameInstance(String name, String gameId, String creatorId, Map<String, Object> attributes) {
        return new TicTacToeGame(name, gameId, creatorId);
    }

}
