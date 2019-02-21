package me.ericjiang.frontiersmen.tictactoe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Sets;

public class TicTacToeBoard<T> {

    private final int size;

    /**
     * Array representation of a grid, where each space may contain a mark
     * placed by one player. Each mark is represented by the identifying T
     * of the player who placed it. Null values represent empty spaces.
     */
    private final T[][] spaces;

    /**
     * Creates a board with size 3x3
     */
    public TicTacToeBoard() {
        this(3);
    }

    /**
     * Creates a board with size n x n
     */
    public TicTacToeBoard(int n) {
        size = n;
        spaces = (T[][]) new Object[n][n];
    }

    /**
     * For testing.
     */
    protected TicTacToeBoard(T[][] spaces) {
        this.size = spaces.length;
        this.spaces = spaces;
    }

    /**
     * Places a mark owned by playerIdentifier in the space located at (row,col).
     *
     * @param row
     * @param col
     * @param playerIdentifier
     */
    public void placeMark(int row, int col, T playerIdentifier) {
        Preconditions.checkArgument(spaces[row][col] == null,
                "Cannot place mark at (%s, %s). Space is taken!", row, col);
        spaces[row][col] = playerIdentifier;
        System.out.println(this);
    }

    public boolean isFull() {
        for (T[] row : spaces) {
            if (Arrays.stream(row).anyMatch(space -> space == null)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Space-restricted victory determination based on the last move.
     */
    public boolean checkVictory(int row, int col) {
        final T player = spaces[row][col];
        if (checkRowVictory(row).map(winner -> winner.equals(player)).orElse(false)) {
            return true;
        }
        if (checkColumnVictory(col).map(winner -> winner.equals(player)).orElse(false)) {
            return true;
        }
        if (row == col && checkDiagonalVictory(false).map(winner -> winner.equals(player)).orElse(false)) {
            return true;
        }
        if (row + col + 1 == size && checkDiagonalVictory(true).map(winner -> winner.equals(player)).orElse(false)) {
            return true;
        }
        return false;
    }

    public Optional<T> checkVictory() {
        for (int row = 0; row < size; row++) {
            final Optional<T> winner = checkRowVictory(row);
            if (winner.isPresent()) {
                return winner;
            }
        }

        for (int col = 0; col < size; col++) {
            final Optional<T> winner = checkColumnVictory(col);
            if (winner.isPresent()) {
                return winner;
            }
        }

        final Optional<T> diagonalWinner = checkDiagonalVictory(false);
        if (diagonalWinner.isPresent()) {
            return diagonalWinner;
        }

        final Optional<T> antiDiagonalWinner = checkDiagonalVictory(true);
        if (antiDiagonalWinner.isPresent()) {
            return antiDiagonalWinner;
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        String rowSeparator = Collections.nCopies(size, "---").stream().collect(Collectors.joining("+", "\n", "\n"));
        return Arrays.stream(spaces)
                .map(row -> Arrays.stream(row)
                        .map(space -> space != null ? space.toString() : " ")
                        .collect(Collectors.joining(" | ", " ", " ")))
                .collect(Collectors.joining(rowSeparator));
    }

    private Optional<T> checkRowVictory(int row) {
        try {
            return Arrays.stream(spaces[row]).distinct().collect(MoreCollectors.toOptional());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    private Optional<T> checkColumnVictory(int col) {
        try {
            return Arrays.stream(spaces).map(row -> row[col]).distinct().collect(MoreCollectors.toOptional());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /**
     * Checks the diagonal or anti-diagonal for a victory.
     */
    private Optional<T> checkDiagonalVictory(boolean anti) {
        final Set<T> identifiers = Sets.newHashSet();
        for (int row = 0; row < size; row++) {
            int col;
            if (anti) {
                col = size - row - 1;
            } else {
                col = row;
            }
            identifiers.add(spaces[row][col]);
        }
        try {
            return Optional.ofNullable(Iterables.getOnlyElement(identifiers));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}
