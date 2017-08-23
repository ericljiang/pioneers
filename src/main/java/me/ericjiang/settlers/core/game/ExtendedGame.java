package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.player.PlayerDao;

public class ExtendedGame extends Game {

    public ExtendedGame(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao, boolean newGame) {
        super(id, creationTime, name, gameDao, boardDao, playerDao, newGame);
    }

    @Override
    public void initializeBoard() {

    }

    @Override
    public String getExpansion() {
        return GameFactory.EXTENDED;
    }

    @Override
    public int getMinPlayers() {
        return 5;
    }

    @Override
    public int getMaxPlayers() {
        return 6;
    }
}
