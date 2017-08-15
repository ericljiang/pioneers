package me.ericjiang.settlers.core.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ericjiang.settlers.core.game.Game;

/**
 * An Action that originates from a player
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class PlayerAction extends Action {

    private String playerId;

    private String playerName;

    public abstract void accept(Game game);

}
