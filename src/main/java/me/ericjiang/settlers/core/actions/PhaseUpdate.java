package me.ericjiang.settlers.core.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;

@Getter
@AllArgsConstructor
public class PhaseUpdate extends Action {

    private Phase phase;

    private Color activePlayer;

}
