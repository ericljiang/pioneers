package me.ericjiang.frontiersmen.tictactoe;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.PlayerEvent;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameSummary;
import me.ericjiang.frontiersmen.library.game.GameUpdateEvent;
import me.ericjiang.frontiersmen.library.player.Player;

@Slf4j
@NoArgsConstructor
@SuppressWarnings("unused")
public class TicTacToeGame extends Game {

    private Integer[][] board;

    private boolean isOver;

    private Player victor;

    public TicTacToeGame(String name, String gameId, String creatorId) {
        super(name, gameId, creatorId);
        this.board = new Integer[3][3];
        this.isOver = false;
        this.victor = null;
        setEventHandlers();
    }

    @Override
    public GameSummary summarize() {
        return new TicTacToeGameSummary(this);
    }

    @Override
    public int minimumPlayers() {
        return 2;
    }

    @Override
    public int maximumPlayers() {
        return 2;
    }

    @Override
    protected Player createPlayer(String playerId, int seat) {
        return new Player(playerId, seat);
    }

    private void setEventHandlers() {
        on(PlaceMarkEvent.class, e -> {
            Preconditions.checkState(!isOver);

            final String playerId = e.getPlayerId();
            final Player player = getPlayers().get(playerId);
            validateCurrentPlayer(player);

            final int seat = player.getSeat();
            final int row = e.getRow();
            final int col = e.getCol();

            board[row][col] = seat;

            if (hasVictory()) {
                isOver = true;
                victor = player;
                log.info("Player {} won", playerId);
            } else if (boardIsFull()) {
                isOver = true;
                log.info("Stalemate");
            } else {
                endTurn();
            }
            broadcast(new GameUpdateEvent(this));
        });
    }

    private boolean hasVictory() {
        // check rows
        for (Integer[] row : board) {
            final boolean rowIsFull = !Arrays.stream(row)
                    .anyMatch(e -> e == null);
            final int distinctElements = (int) Arrays.stream(row)
                    .distinct()
                    .count();
            if (rowIsFull && distinctElements == 1) {
                return true;
            }
        }
        // check columns
        for (int col = 0; col < 3; col++) {
            final int colIndex = col; // for use in lambda
            final boolean colIsFull = !Arrays.stream(board)
                    .map(row -> row[colIndex])
                    .anyMatch(e -> e == null);
            final int distinctElements = (int) Arrays.stream(board)
                    .map(row -> row[colIndex])
                    .distinct()
                    .count();
            if (colIsFull && distinctElements == 1) {
                return true;
            }
        }
        // check right diagonal
        final Set<Integer> rightDiagonal = Sets.newHashSet();
        for (int i = 0; i < 3; i++) {
            rightDiagonal.add(board[i][i]);
        }
        if (rightDiagonal.size() == 1 && !rightDiagonal.contains(null)) {
            return true;
        }
        // check left diagonal
        final Set<Integer> leftDiagonal = Sets.newHashSet();
        for (int i = 0; i < 3; i++) {
            leftDiagonal.add(board[i][2 - 1]);
        }
        if (leftDiagonal.size() == 1 && !leftDiagonal.contains(null)) {
            return true;
        }

        return false;
    }

    private boolean boardIsFull() {
        for (Integer[] row : board) {
            if (Arrays.stream(row).anyMatch(e -> e == null)) {
                return false;
            }
        }
        return true;
    }
}
