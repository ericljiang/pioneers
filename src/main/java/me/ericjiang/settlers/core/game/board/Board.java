package me.ericjiang.settlers.core.game.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Board {

    // TODO: replace with DAOs
    private Map<Tile.Coordinates, Tile> tiles;
    private Map<Edge.Coordinates, Edge> edges;
    private Map<Intersection.Coordinates, Intersection> intersections;

    public Board() {
        tiles = new HashMap<Tile.Coordinates, Tile>();
        edges = new HashMap<Edge.Coordinates, Edge>();
        intersections = new HashMap<Intersection.Coordinates, Intersection>();
        assemble();
    }

    /**
     * Arrange tiles and populate edges and intersections
     */
    public abstract void assemble();

    public Tile getTile(Tile.Coordinates coordinates) {
        return tiles.get(coordinates);
    }

    public Set<Tile> getNeighborsOf(Tile tile) {
        return tile.getCoordinates().getNeighbors().stream()
                .map(c -> tiles.get(c))
                .collect(Collectors.toSet());
    }

    public Edge getEdge(Edge.Coordinates coordinates) {
        return edges.get(coordinates);
    }

    public Set<Edge> getEdgesOf(Tile tile) {
        return tile.getCoordinates().getEdges().stream()
                .map(c -> edges.get(c))
                .collect(Collectors.toSet());
    }

    public Intersection getIntersection(Intersection.Coordinates coordinates) {
        return intersections.get(coordinates);
    }

    public Set<Intersection> getCornersOf(Tile tile) {
        return tile.getCoordinates().getCorners().stream()
                .map(c -> intersections.get(c))
                .collect(Collectors.toSet());
    }

}