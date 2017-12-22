package me.ericjiang.settlers.core.actions;

import lombok.Getter;
import me.ericjiang.settlers.core.board.Intersection.Coordinates.Direction;
import me.ericjiang.settlers.core.game.Game;

/**
 * @author ejiang
 */
@Getter
public class BuildSettlementAction extends PlayerAction {

    private int column;

    private int row;

    private Direction direction;

    @Override
    public void accept(Game game) {
        game.handleBuildAction(this);
    }
}
