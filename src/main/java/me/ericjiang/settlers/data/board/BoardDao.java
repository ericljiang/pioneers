package me.ericjiang.settlers.data.board;

import java.util.Set;
import java.util.stream.Collectors;
import me.ericjiang.settlers.core.board.Edge;
import me.ericjiang.settlers.core.board.Intersection;
import me.ericjiang.settlers.core.board.Tile;

public interface BoardDao {

    Tile getTile(String gameId, Tile.Coordinates coordinates);

    void putTile(String gameId, Tile.Coordinates coordinates, Tile tile);

    Edge getEdge(String gameId, Edge.Coordinates coordinates);

    void putEdge(String gameId, Edge.Coordinates coordinates, Edge edge);

    Intersection getIntersection(String gameId, Intersection.Coordinates coordinates);

    void putIntersection(String gameId, Intersection.Coordinates coordinates, Intersection intersection);

    default Set<Tile> getNeighborsOf(String gameId, Tile tile) {
        return tile.getCoordinates().getNeighbors().stream()
                .map(c -> getTile(gameId, c))
                .collect(Collectors.toSet());
    }

    default Set<Edge> getEdgesOf(String gameId, Tile tile) {
        return tile.getCoordinates().getEdges().stream()
                .map(c -> getEdge(gameId, c))
                .collect(Collectors.toSet());
    }

    default Set<Intersection> getCornersOf(String gameId, Tile tile) {
        return tile.getCoordinates().getCorners().stream()
                .map(c -> getIntersection(gameId, c))
                .collect(Collectors.toSet());
    }

}
