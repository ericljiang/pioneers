package me.ericjiang.frontiersmen.tictactoe;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.Getter;
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

    private TicTacToeBoard<Integer> board;

    @VisibleForTesting
    @Getter(value = AccessLevel.PACKAGE)
    private boolean isOver;

    @VisibleForTesting
    @Getter(value = AccessLevel.PACKAGE)
    private Player victor;

    public TicTacToeGame(String name, String gameId, String creatorId, TicTacToeBoard<Integer> board) {
        super(name, gameId, creatorId);
        this.board = board;
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
            validateCurrentPlayer(playerId);
            final Player player = getPlayers().get(playerId);

            final int seat = player.getSeat();
            final int row = e.getRow();
            final int col = e.getCol();

            board.placeMark(row, col, seat);

            if (board.checkVictory(row, col)) {
                isOver = true;
                victor = player;
                log.info("Player {} has won.", playerId);
            } else if (board.isFull()) {
                isOver = true;
                log.info("Game has ended in a stalemate.");
            } else {
                endTurn();
            }
            broadcast(new GameUpdateEvent(this));
        });
    }
}
