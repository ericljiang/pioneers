package me.ericjiang.settlers.core.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Intersection {

    @Getter
    @AllArgsConstructor
    public static class Coordinates {
        private int column;
        private int row;
        private Direction direction;

        public static enum Direction { L, R }
    }
}