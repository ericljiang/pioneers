package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import me.ericjiang.settlers.core.actions.BuildSettlementAction;
import me.ericjiang.settlers.core.board.Intersection;
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

    @Override
    public void handleBuildAction(BuildSettlementAction action) {
        // check turn
        validateActionByActivePlayer(action);
        validatePhase(Phase.INITIAL_PLACEMENT, Phase.BUILD);
        Intersection.Coordinates coordinates = new Intersection.Coordinates(
                action.getColumn(), action.getRow(), action.getDirection());
        boardDao.putIntersection(getId(), coordinates, new Intersection(coordinates, action.getPlayerId(), false));
        broadcast(action);
        if (gameDao.getPhase(getId()) == Phase.INITIAL_PLACEMENT) {
            changePhase(Phase.INITIAL_PLACEMENT, true);
        }
    }
}
