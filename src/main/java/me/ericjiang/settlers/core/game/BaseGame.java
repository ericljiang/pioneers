package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.player.PlayerDao;

public class BaseGame extends Game {

    public BaseGame(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao) {
        super(id, creationTime, name, gameDao, boardDao, playerDao);
    }

    @Override
    public void initializeBoard() {

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
}
