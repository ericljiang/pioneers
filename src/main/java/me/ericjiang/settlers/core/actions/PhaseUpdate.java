package me.ericjiang.settlers.core.actions;

import lombok.AllArgsConstructor;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;

@SuppressWarnings("unused")
@AllArgsConstructor
public class PhaseUpdate extends Action {

    private Phase phase;

    private Color activePlayer;

}
