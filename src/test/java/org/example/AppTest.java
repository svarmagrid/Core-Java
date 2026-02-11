package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    // ---------- HELPERS ----------

    private char[][] emptyBoard() {
        char[][] board = new char[3][3];
        for (char[] row : board) {
            java.util.Arrays.fill(row, ' ');
        }
        return board;
    }

    // ---------- isValidPlayer ----------

    @Test
    void validPlayersAreAccepted() {
        assertTrue(App.isValidPlayer("user"));
        assertTrue(App.isValidPlayer("easy"));
        assertTrue(App.isValidPlayer("medium"));
        assertTrue(App.isValidPlayer("hard"));
    }

    @Test
    void invalidPlayersAreRejected() {
        assertFalse(App.isValidPlayer("human"));
        assertFalse(App.isValidPlayer("ai"));
        assertFalse(App.isValidPlayer(""));
    }

    // ---------- isWinner ----------

    @Test
    void detectsRowWin() {
        char[][] board = emptyBoard();
        board[0][0] = 'X';
        board[0][1] = 'X';
        board[0][2] = 'X';

        assertTrue(App.isWinner(board, 'X'));
        assertFalse(App.isWinner(board, 'O'));
    }

    @Test
    void detectsColumnWin() {
        char[][] board = emptyBoard();
        board[0][1] = 'O';
        board[1][1] = 'O';
        board[2][1] = 'O';

        assertTrue(App.isWinner(board, 'O'));
    }

    @Test
    void detectsDiagonalWin() {
        char[][] board = emptyBoard();
        board[0][0] = 'X';
        board[1][1] = 'X';
        board[2][2] = 'X';

        assertTrue(App.isWinner(board, 'X'));
    }

    // ---------- isDraw ----------

    @Test
    void detectsDraw() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', 'X'}
        };

        assertTrue(App.isDraw(board));
        assertFalse(App.isWinner(board, 'X'));
        assertFalse(App.isWinner(board, 'O'));
    }

    @Test
    void notDrawWhenEmptyCellExists() {
        char[][] board = emptyBoard();
        board[0][0] = 'X';

        assertFalse(App.isDraw(board));
    }

    // ---------- computerMove (easy) ----------

    @Test
    void computerMovePlacesSymbol() {
        char[][] board = emptyBoard();

        App.computerMove(board, 'X');

        boolean found = false;
        for (char[] row : board) {
            for (char c : row) {
                if (c == 'X') {
                    found = true;
                }
            }
        }
        assertTrue(found);
    }

    // ---------- mediumMove ----------

    @Test
    void mediumMoveWinsIfPossible() {
        char[][] board = emptyBoard();
        board[0][0] = 'X';
        board[0][1] = 'X';

        App.mediumMove(board, 'X');

        assertEquals('X', board[0][2]);
        assertTrue(App.isWinner(board, 'X'));
    }

    @Test
    void mediumMoveBlocksOpponentWin() {
        char[][] board = emptyBoard();
        board[1][0] = 'O';
        board[1][1] = 'O';

        App.mediumMove(board, 'X');

        assertEquals('X', board[1][2]);
    }

    // ---------- hardMove / minimax ----------

    @Test
    void hardMoveNeverLosesImmediateGame() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'O', 'O', ' '},
                {'X', ' ', ' '}
        };

        App.hardMove(board, 'X');

        // Hard AI must block O from winning
        assertEquals('X', board[1][2]);
    }

    @Test
    void minimaxReturnsWinningScoreForAI() {
        char[][] board = emptyBoard();
        board[0][0] = 'X';
        board[0][1] = 'X';

        int score = App.minimax(board, true, 'X', 'O');

        assertTrue(score > 0);
    }

    @Test
    void minimaxReturnsDrawScore() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', ' '}
        };

        int score = App.minimax(board, true, 'X', 'O');

        assertEquals(0, score);
    }
}

