package me.ericjiang.settlers.core.board;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Intersection {

    private Coordinates coordinates;

    private String owner;

    private boolean isUpgraded;

    @Getter
    @AllArgsConstructor
    public static class Coordinates {
        private int column;
        private int row;
        private Direction direction;

        public static enum Direction {
            @SerializedName("n") N,
            @SerializedName("s") S;

            @Override
            public String toString() {
                return name().toLowerCase();
            }

            public static Direction fromString(String string) {
                return valueOf(string.toUpperCase());
            }
        }
    }
}
