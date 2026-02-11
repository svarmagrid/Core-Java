package org.example;

import java.util.*;

public class App  {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            System.out.print("Input command:");
            String line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("exit")) {
                break;
            }

            if (parts.length != 3 || !parts[0].equals("start")) {
                System.out.println("Bad parameters!");
                continue;
            }

            String xPlayer = parts[1];
            String oPlayer = parts[2];

            if (!isValidPlayer(xPlayer) || !isValidPlayer(oPlayer)) {
                System.out.println("Bad parameters!");
                continue;
            }

            playGame(xPlayer, oPlayer);
        }
    }

    public static boolean isValidPlayer(String player) {
        return player.equals("user") ||
                player.equals("easy") ||
                player.equals("medium") ||
                player.equals("hard");
    }

    public static void playGame(String xPlayer, String oPlayer) {
        char[][] board = new char[3][3];
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }

        printBoard(board);

        while (true) {
            makeMove(xPlayer, board, 'X');
            printBoard(board);
            if (checkEnd(board)) break;

            makeMove(oPlayer, board, 'O');
            printBoard(board);
            if (checkEnd(board)) break;
        }
    }

    public static void printBoard(char[][] board) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static void makeMove(String player, char[][] board, char symbol) {
        switch (player) {
            case "user":
                userMove(board, symbol);
                break;
            case "easy":
                System.out.println("Making move level \"easy\"");
                computerMove(board, symbol);
                break;
            case "medium":
                System.out.println("Making move level \"medium\"");
                mediumMove(board, symbol);
                break;
            case "hard":
                System.out.println("Making move level \"hard\"");
                hardMove(board, symbol);
                break;
            default:
                System.out.println("Bad parameters!");
                break;
        }
    }

    public static boolean checkEnd(char[][] board) {
        if (isWinner(board, 'X')) {
            System.out.println("X wins");
            return true;
        }
        if (isWinner(board, 'O')) {
            System.out.println("O wins");
            return true;
        }
        if (isDraw(board)) {
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    public static boolean isDraw(char[][] board) {
        for (char[] row : board) {
            for (char c : row) {
                if (c == ' ') return false;
            }
        }
        return true;
    }

    public static boolean isWinner(char[][] board, char s) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == s && board[i][1] == s && board[i][2] == s) return true;
            if (board[0][i] == s && board[1][i] == s && board[2][i] == s) return true;
        }
        return (board[0][0] == s && board[1][1] == s && board[2][2] == s) ||
                (board[2][0] == s && board[1][1] == s && board[0][2] == s);
    }

    public static void userMove(char[][] board, char symbol) {
        while (true) {
            System.out.print("Enter the coordinates:");
            String line= sc.nextLine().trim();
            String[] nums = line.split("\\s+");

            if (nums.length != 2) {
                System.out.println("You should enter numbers!");
                continue;
            }

            int x, y;
            try {
                x = Integer.parseInt(nums[0]);
                y = Integer.parseInt(nums[1]);
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
                continue;
            }

            if (x < 1 || x > 3 || y < 1 || y > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            int r = x - 1, c = y - 1;
            if (board[r][c] != ' ') {
                System.out.println("This cell is occupied!");
                continue;
            }

            board[r][c] = symbol;
            break;
        }
    }

    public static void computerMove(char[][] board, char symbol) {
        while (true) {
            int x = random.nextInt(3);
            int y = random.nextInt(3);
            if (board[x][y] == ' ') {
                board[x][y] = symbol;
                break;
            }
        }
    }

    public static void mediumMove(char[][] board, char symbol) {
        int[] move = findWinningMove(board, symbol);
        if (move != null) {
            board[move[0]][move[1]] = symbol;
            return;
        }

        char opp = (symbol == 'X') ? 'O' : 'X';
        move = findWinningMove(board, opp);
        if (move != null) {
            board[move[0]][move[1]] = symbol;
            return;
        }

        computerMove(board, symbol);
    }

    public static int[] findWinningMove(char[][] board, char symbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = symbol;
                    if (isWinner(board, symbol)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return null;
    }

    // ---------- HARD (MINIMAX) ----------

    public static void hardMove(char[][] board, char symbol) {
        char opponent = (symbol == 'X') ? 'O' : 'X';
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = symbol;
                    int score = minimax(board, false, symbol, opponent);
                    board[i][j] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        board[bestMove[0]][bestMove[1]] = symbol;
    }

    public static int minimax(char[][] board, boolean isMax, char ai, char human) {
        if (isWinner(board, ai)) return 10;
        if (isWinner(board, human)) return -10;
        if (isDraw(board)) return 0;

        int best;

        if (isMax) {
            best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = ai;
                        best = Math.max(best, minimax(board, false, ai, human));
                        board[i][j] = ' ';
                    }
                }
            }
        } else {
            best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = human;
                        best = Math.min(best, minimax(board, true, ai, human));
                        board[i][j] = ' ';
                    }
                }
            }
        }
        return best;
    }
}
