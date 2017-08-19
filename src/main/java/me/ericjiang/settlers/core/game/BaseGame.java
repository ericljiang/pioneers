package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import me.ericjiang.settlers.core.board.Tile;
import me.ericjiang.settlers.core.board.Tile.Resource;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.player.PlayerDao;

public class BaseGame extends Game {

    public BaseGame(String id, LocalDateTime creationTime, String name,
            GameDao gameDao, BoardDao boardDao, PlayerDao playerDao) {
        super(id, creationTime, name, gameDao, boardDao, playerDao);
    }

    /**
     * Four (4) Fields (Grain Resource) Hexes.
     * Four (4) Forest (Lumber Resource) Hexes.
     * Four (4) Pasture (Wool Resource) Hexes.
     * Three (3) Mountains (Ore Resource) Hexes.
     * Three (3) Hills (Brick Resource) Hexes.
     * One (1) Desert (No Resource) Hex.
     */
    @Override
    public void initializeBoard() {
        // create tiles
        LinkedList<Resource> resources = new LinkedList<Resource>();
        for (int i = 0; i <= 3; i++) {
            resources.add(Resource.GRAIN);
            resources.add(Resource.LUMBER);
            resources.add(Resource.WOOL);
        }
        for (int i = 0; i <= 2; i++) {
            resources.add(Resource.ORE);
            resources.add(Resource.BRICK);
        }
        resources.add(Resource.NONE);
        Collections.shuffle(resources);

        // create number tokens
        LinkedList<Integer> tokens = new LinkedList<Integer>();
        tokens.add(2);
        for (int i = 3; i <= 11; i++) {
            tokens.add(i);
            tokens.add(i);
        }
        tokens.add(12);
        Collections.shuffle(tokens);

        // place tiles
        for (int column = -2; column <= 2; column++) {
            for (int row = -2; row <= 2; row++) {
                if (column + row >= -2 && column + row <= 2) {
                    Resource resource = resources.remove();
                    Tile.Coordinates coordinates = new Tile.Coordinates(column, row);
                    int numberToken = resource == Resource.NONE ? 0 : tokens.remove();
                    Tile tile = new Tile(coordinates, resource, numberToken);
                    boardDao.putTile(id, coordinates, tile);
                    //TODO: putTiles
                }
            }
        }
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
