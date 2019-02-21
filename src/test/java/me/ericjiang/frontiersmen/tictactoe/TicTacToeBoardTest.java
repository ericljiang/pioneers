package me.ericjiang.frontiersmen.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TicTacToeBoardTest {

    private TicTacToeBoard<String> board;

    @Test
    public void testIsFull() {
        board = new TicTacToeBoard<String>(new String[][] {
            { "X", "X", "X" },
            { "X", "X", "X" },
            { "X", "X", "X" }
        });
        assertTrue(board.isFull());

        board = new TicTacToeBoard<String>();
        assertFalse(board.isFull());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowPlacingOverPreviousMark() {
        board = new TicTacToeBoard<String>();

        board.placeMark(0, 0, "X");
        board.placeMark(0, 0, "X");
    }

    @Test
    public void shouldDetectRowVictory() {
        board = new TicTacToeBoard<String>(new String[][] {
            { null, null, null },
            { null, null, null },
            { "X", "X", "X" }
        });
        assertEquals("X", board.checkVictory().get());
    }

    @Test
    public void shouldDetectColumnVictory() {
        board = new TicTacToeBoard<String>(new String[][] {
            { null, null, "X" },
            { null, null, "X" },
            { null, null, "X" }
        });
        assertEquals("X", board.checkVictory().get());
    }

    @Test
    public void shouldDetectDiagonalVictory() {
        board = new TicTacToeBoard<String>(new String[][] {
            { "X", null, null },
            { null, "X", null },
            { null, null, "X" }
        });
        assertEquals("X", board.checkVictory().get());
    }

    @Test
    public void shouldDetectAntiDiagonalVictory() {
        board = new TicTacToeBoard<String>(new String[][] {
            { null, null, "X" },
            { null, "X", null },
            { "X", null, null }
        });
        assertEquals("X", board.checkVictory().get());
    }

    @Test
    public void shouldNotReturnWinnerIfNoVictory() {
        board = new TicTacToeBoard<String>();
        assertFalse(board.checkVictory().isPresent());
    }

    @Test
    public void shouldDetectVictoryBasedOnLastMove() {
        board = new TicTacToeBoard<String>(new String[][] {
            { null, null, "X" },
            { null, "X", null },
            { "X", null, null }
        });
        assertTrue(board.checkVictory(0, 2));

        board = new TicTacToeBoard<String>(new String[][] {
            { "X", null, "O" },
            { "X", null, "O" },
            { "X", null, "O" }
        });
        assertTrue(board.checkVictory(0, 2));
    }

}
