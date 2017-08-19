package me.ericjiang.settlers.core.board;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Tile {

    private Coordinates coordinates; // TODO: can this be removed?

    private Resource resource;

    private int numberToken;

    public enum Resource { ORE, GRAIN, LUMBER, WOOL, BRICK, NONE }

    /**
     * http://www-cs-students.stanford.edu/~amitp/game-programming/grids/
     */
    @Getter
    @AllArgsConstructor
    public static class Coordinates {
        private int column;
        private int row;

        public Set<Coordinates> getNeighbors() {
            // (u,v) → (u,v+1) (u+1,v) (u+1,v-1) (u,v-1) (u-1,v) (u-1,v+1)
            Set<Coordinates> neighbors = new HashSet<Coordinates>();
            neighbors.add(new Coordinates(column, row + 1));
            neighbors.add(new Coordinates(column + 1, row));
            neighbors.add(new Coordinates(column + 1, row - 1));
            neighbors.add(new Coordinates(column, row - 1));
            neighbors.add(new Coordinates(column - 1, row));
            neighbors.add(new Coordinates(column - 1, row + 1));
            return neighbors;
        }

        public Set<Edge.Coordinates> getEdges() {
            // (u,v) → (u,v,N) (u,v,E) (u+1,v-1,W) (u,v-1,N) (u-1,v,E) (u,v,W)
            Set<Edge.Coordinates> edges = new HashSet<Edge.Coordinates>();
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.N));
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.E));
            edges.add(new Edge.Coordinates(column + 1, row - 1, Edge.Coordinates.Direction.W));
            edges.add(new Edge.Coordinates(column, row - 1, Edge.Coordinates.Direction.N));
            edges.add(new Edge.Coordinates(column - 1, row, Edge.Coordinates.Direction.E));
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.W));
            return edges;
        }

        public Set<Intersection.Coordinates> getCorners() {
            // (u,v) → (u+1,v,L) (u,v,R) (u+1,v-1,L) (u-1,v,R) (u,v,L) (u-1,v+1,R)
            Set<Intersection.Coordinates> corners = new HashSet<Intersection.Coordinates>();
            corners.add(new Intersection.Coordinates(column + 1, row, Intersection.Coordinates.Direction.L));
            corners.add(new Intersection.Coordinates(column, row, Intersection.Coordinates.Direction.R));
            corners.add(new Intersection.Coordinates(column + 1, row - 1, Intersection.Coordinates.Direction.L));
            corners.add(new Intersection.Coordinates(column - 1, row, Intersection.Coordinates.Direction.R));
            corners.add(new Intersection.Coordinates(column, row, Intersection.Coordinates.Direction.L));
            corners.add(new Intersection.Coordinates(column - 1, row + 1, Intersection.Coordinates.Direction.R));
            return corners;
        }
    }
}
