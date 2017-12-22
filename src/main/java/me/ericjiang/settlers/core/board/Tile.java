package me.ericjiang.settlers.core.board;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

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

    public enum Resource {
        @SerializedName("ore") ORE,
        @SerializedName("grain") GRAIN,
        @SerializedName("lumber") LUMBER,
        @SerializedName("wool") WOOL,
        @SerializedName("brick") BRICK,
        @SerializedName("none") NONE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Resource fromString(String string) {
            return valueOf(string.toUpperCase());
        }
    }

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
            // (u,v) → (u,v,NE) (u,v,E) (u,v,SE) (u-1,v+1,NE) (u-1,v,E) (u,v-1,SE)
            Set<Edge.Coordinates> edges = new HashSet<Edge.Coordinates>();
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.NE));
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.E));
            edges.add(new Edge.Coordinates(column, row, Edge.Coordinates.Direction.SE));
            edges.add(new Edge.Coordinates(column - 1, row + 1, Edge.Coordinates.Direction.NE));
            edges.add(new Edge.Coordinates(column - 1, row, Edge.Coordinates.Direction.E));
            edges.add(new Edge.Coordinates(column, row - 1, Edge.Coordinates.Direction.SE));
            return edges;
        }

        public Set<Intersection.Coordinates> getCorners() {
            // (u,v) → (u,v,N) (u+1,v-1,S) (u,v+1,N) (u,v,S) (u-1,v+1,N) (u,v-1,S)
            Set<Intersection.Coordinates> corners = new HashSet<Intersection.Coordinates>();
            corners.add(new Intersection.Coordinates(column, row, Intersection.Coordinates.Direction.N));
            corners.add(new Intersection.Coordinates(column + 1, row - 1, Intersection.Coordinates.Direction.S));
            corners.add(new Intersection.Coordinates(column, row + 1, Intersection.Coordinates.Direction.N));
            corners.add(new Intersection.Coordinates(column, row, Intersection.Coordinates.Direction.S));
            corners.add(new Intersection.Coordinates(column - 1, row + 1, Intersection.Coordinates.Direction.N));
            corners.add(new Intersection.Coordinates(column, row - 1, Intersection.Coordinates.Direction.S));
            return corners;
        }
    }
}
