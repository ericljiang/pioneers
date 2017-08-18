package me.ericjiang.settlers.core.actions;

import java.util.Collection;
import lombok.AllArgsConstructor;
import me.ericjiang.settlers.core.board.Tile;

@AllArgsConstructor
public class GameUpdate extends Action {

    private String expansion;

    private int minPlayers;

    private int maxPlayers;

    private Collection<Tile> tiles;

}
