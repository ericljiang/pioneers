package me.ericjiang.settlers.core.actions;

import java.util.Collection;
import lombok.AllArgsConstructor;
import me.ericjiang.settlers.core.board.Tile;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;

@AllArgsConstructor
@SuppressWarnings("unused")
public class GameUpdate extends Action {

    private String gameId;

    private String gameName;

    private String expansion;

    private int minPlayers;

    private int maxPlayers;

    private Collection<Tile> tiles;

    private Phase phase;

    private Color activePlayer;

}
