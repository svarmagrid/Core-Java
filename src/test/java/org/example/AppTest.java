package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

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

    @Test
    void testXWins() {
        char[][] board = {
                {'X', 'X', 'X'},
                {'O', 'O', ' '},
                {' ', ' ', ' '}
        };

        boolean result = App.checkEnd(board);
        assertTrue(result);
    }

    @Test
    void testOWins() {
        char[][] board = {
                {'O', 'X', 'X'},
                {'O', 'X', ' '},
                {'O', ' ', ' '}
        };

        boolean result = App.checkEnd(board);
        assertTrue(result);
    }

    @Test
    void testDraw() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'X', 'O', 'O'},
                {'O', 'X', 'X'}
        };

        boolean result = App.checkEnd(board);
        assertTrue(result);
    }

    @Test
    void testGameNotFininshed() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'X', ' ', 'O'},
                {'O', 'X', ' '}
        };

        boolean result = App.checkEnd(board);
        assertFalse(result);
    }

    @Test
    void testEasyMovePrintsMessage() {
        char[][] board = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        App.makeMove("easy", board, 'X');

        System.setOut(originalOut);

        assertTrue(outputStream.toString().contains("Making move level \"easy\""));
    }

    @Test
    void testBadParameters() {
        char[][] board = new char[3][3];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        App.makeMove("invalid", board, 'X');

        System.setOut(originalOut);

        assertTrue(outputStream.toString().contains("Bad parameters!"));
    }

    @Test
    void testPrintBoard() {
        char[][] board = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'X', 'O', 'X'}
        };

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Call method
        App.printBoard(board);

        // Restore original System.out
        System.setOut(originalOut);

        String expectedOutput =
                "---------\n" +
                        "| X O X |\n" +
                        "| O X O |\n" +
                        "| X O X |\n" +
                        "---------\n";

        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testPlayGameEasyVsEasy() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        App.playGame("easy", "easy");

        System.setOut(originalOut);

        String console = outputStream.toString();
        assertTrue(console.contains("Making move level \"easy\""));
        assertTrue(console.contains("X wins") || console.contains("O wins") || console.contains("Draw"));
    }

    @Test
    void testUserMoveValidInput() {
        char[][] board = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        String simulatedInput = "2 2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        App.sc = new Scanner(System.in);

        App.userMove(board, 'X');

        assertEquals('X', board[1][1]);
    }

    @Test
    void testUserMoveInvalidThenValid() {
        char[][] board = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        String simulatedInput = "a b\n2 2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        App.sc = new Scanner(System.in);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        App.userMove(board, 'X');

        System.setOut(originalOut);

        assertTrue(outputStream.toString().contains("You should enter numbers!"));
        assertEquals('X', board[1][1]);
    }


}

