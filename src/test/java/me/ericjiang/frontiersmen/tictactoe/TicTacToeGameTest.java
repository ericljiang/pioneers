package me.ericjiang.frontiersmen.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.game.OutOfTurnEventException;
import me.ericjiang.frontiersmen.library.player.Player;

public class TicTacToeGameTest {

    private static final String NAME = "name";

    private static final String GAME_ID = "gameId";

    private static final String CREATOR_ID = "creator";

    private static final String PLAYER_ID_1 = "1";

    private static final String PLAYER_ID_2 = "2";

    private static final Player PLAYER_1 = new Player(PLAYER_ID_1);

    private static final Player PLAYER_2 = new Player(PLAYER_ID_2);

    private static final Player[] PLAYERS = { PLAYER_1, PLAYER_2 };

    private TicTacToeBoard<Integer> board;

    private TicTacToeGame game;

    @Before
    public void before() {
        this.board = new TicTacToeBoard<>();
        this.game = new TicTacToeGame(NAME, GAME_ID, CREATOR_ID, board);
        game.setPlayers(PLAYERS);
    }

    @Test
    public void shouldEndTurn() {
        Player firstPlayer = game.getCurrentPlayer();
        PlaceMarkEvent move = new PlaceMarkEvent();
        move.setPlayerId(firstPlayer.getId());
        move.setRow(0);
        move.setCol(0);
        game.handleEvent(move);

        assertNotEquals(firstPlayer, game.getCurrentPlayer());
    }

    @Test(expected = OutOfTurnEventException.class)
    public void shouldNotAllowOutOfTurnMoves() {
        PlaceMarkEvent move = new PlaceMarkEvent();
        move.setPlayerId(game.getCurrentPlayer().getId());
        move.setRow(0);
        move.setCol(0);
        game.handleEvent(move);
        game.handleEvent(move);
    }

    @Test
    public void shouldAllowInTurnMoves() {
        PlaceMarkEvent move1 = new PlaceMarkEvent();
        move1.setPlayerId(game.getCurrentPlayer().getId());
        move1.setRow(0);
        move1.setCol(0);
        game.handleEvent(move1);

        PlaceMarkEvent move2 = new PlaceMarkEvent();
        move2.setPlayerId(game.getCurrentPlayer().getId());
        move2.setRow(0);
        move2.setCol(1);
    }

    @Test
    public void shouldDetectVictory() {
        TicTacToeBoard<Integer> almostCompleteBoard = new TicTacToeBoard<>(new Integer[][] {
            { null, 0, 0 },
            { null, null, null },
            { null, null, null }
        });
        game = new TicTacToeGame(NAME, GAME_ID, CREATOR_ID, almostCompleteBoard);
        game.setPlayers(PLAYERS);

        if (game.getCurrentPlayer().getSeat() != 0) {
            PlaceMarkEvent preMove = new PlaceMarkEvent();
            preMove.setPlayerId(game.getCurrentPlayer().getId());
            preMove.setRow(1);
            preMove.setCol(0);
            game.handleEvent(preMove);
        }

        PlaceMarkEvent move = new PlaceMarkEvent();
        move.setPlayerId(game.getCurrentPlayer().getId());
        move.setRow(0);
        move.setCol(0);
        game.handleEvent(move);

        System.out.println(almostCompleteBoard);

        assertTrue(game.isOver());
        assertEquals(move.getPlayerId(), game.getVictor().getId());
    }

    @Test
    public void shouldDetectDraw() {
        TicTacToeBoard<Integer> almostCompleteBoard = new TicTacToeBoard<>(new Integer[][] {
            { 0, 0, 1 },
            { 1, 1, 0 },
            { 0, null, null }
        });
        game = new TicTacToeGame(NAME, GAME_ID, CREATOR_ID, almostCompleteBoard);
        game.setPlayers(PLAYERS);

        PlaceMarkEvent move1 = new PlaceMarkEvent();
        move1.setPlayerId(game.getCurrentPlayer().getId());
        move1.setRow(2);
        move1.setCol(1);
        game.handleEvent(move1);

        PlaceMarkEvent move2 = new PlaceMarkEvent();
        move2.setPlayerId(game.getCurrentPlayer().getId());
        move2.setRow(2);
        move2.setCol(2);
        game.handleEvent(move2);

        System.out.println(almostCompleteBoard);

        assertTrue(game.isOver());
        assertNull(game.getVictor());
    }
}
