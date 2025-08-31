package com.game.main;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class GameBuilder {
    static String[] board;
    static String turn;
    static int xWins = 0, oWins = 0, draws = 0;
    static Scanner in = new Scanner(System.in);
    static Random rand = new Random();

    // Check winner
    static String checkWinner() {
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // columns
                {0, 4, 8}, {2, 4, 6}              // diagonals
        };

        for (int[] combo : winCombinations) {
            String line = board[combo[0]] + board[combo[1]] + board[combo[2]];
            if (line.equals("XXX")) return "X";
            if (line.equals("OOO")) return "O";
        }

        // Check draw
        for (int i = 0; i < 9; i++) {
            if (board[i].equals(String.valueOf(i + 1))) {
                return null; // still moves left
            }
        }
        return "draw";
    }

    // Print the board
    static void printBoard() {
        System.out.println();
        System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2]);
        System.out.println("---+---+---");
        System.out.println(" " + board[3] + " | " + board[4] + " | " + board[5]);
        System.out.println("---+---+---");
        System.out.println(" " + board[6] + " | " + board[7] + " | " + board[8]);
        System.out.println();
    }

    // Reset board
    static void resetBoard() {
        board = new String[9];
        for (int a = 0; a < 9; a++) {
            board[a] = String.valueOf(a + 1);
        }
        turn = "X";
    }

    // Player Move
    static void playerMove() {
        int numInput;
        while (true) {
            try {
                numInput = in.nextInt();
                if (!(numInput > 0 && numInput <= 9)) {
                    System.out.println("Invalid input; Re-enter slot number:");
                    continue;
                }
                if (board[numInput - 1].equals(String.valueOf(numInput))) {
                    board[numInput - 1] = turn;
                    break;
                } else {
                    System.out.println("Slot already taken; Re-enter slot number:");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input; re-enter slot number:");
                in.nextLine();
            }
        }
    }

    // Computer Move (Random AI)
    // Computer Move (Smart AI: tries to win or block)
    static void computerMove() {
        // Winning combinations
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };

        // 1. Try to win
        for (int[] combo : winCombinations) {
            String line = board[combo[0]] + board[combo[1]] + board[combo[2]];
            if (line.chars().filter(ch -> ch == 'O').count() == 2) {
                for (int index : combo) {
                    if (board[index].equals(String.valueOf(index + 1))) {
                        board[index] = "O";
                        System.out.println("Computer chose slot: " + (index + 1) + " (Winning Move)");
                        return;
                    }
                }
            }
        }

        // 2. Block opponent's winning move
        for (int[] combo : winCombinations) {
            String line = board[combo[0]] + board[combo[1]] + board[combo[2]];
            if (line.chars().filter(ch -> ch == 'X').count() == 2) {
                for (int index : combo) {
                    if (board[index].equals(String.valueOf(index + 1))) {
                        board[index] = "O";
                        System.out.println("Computer chose slot: " + (index + 1) + " (Blocking Move)");
                        return;
                    }
                }
            }
        }

        // 3. Otherwise take center if free
        if (board[4].equals("5")) {
            board[4] = "O";
            System.out.println("Computer chose slot: 5 (Center)");
            return;
        }

        // 4. Otherwise choose random
        int move;
        while (true) {
            move = rand.nextInt(9);
            if (board[move].equals(String.valueOf(move + 1))) {
                board[move] = "O";
                System.out.println("Computer chose slot: " + (move + 1));
                break;
            }
        }
    }


    // Game loop
    static void playGame(boolean vsComputer) {
        resetBoard();
        String winner = null;
        System.out.println("Welcome to Tic Tac Toe!");
        printBoard();

        while (winner == null) {
            System.out.println(turn + "'s turn; enter a slot number to place " + turn + " in:");
            if (vsComputer && turn.equals("O")) {
                computerMove();
            } else {
                playerMove();
            }

            printBoard();
            winner = checkWinner();

            if (winner == null) {
                turn = turn.equals("X") ? "O" : "X";
            }
        }

        // Final result
        if (winner.equalsIgnoreCase("draw")) {
            System.out.println("It's a draw!");
            draws++;
        } else {
            System.out.println("Congratulations! " + winner + " has won!");
            if (winner.equals("X")) xWins++;
            else oWins++;
        }

        // Scoreboard
        System.out.println("\nScoreboard:");
        System.out.println("X Wins: " + xWins);
        System.out.println("O Wins: " + oWins);
        System.out.println("Draws : " + draws);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nChoose Game Mode:");
            System.out.println("1. Player vs Player");
            System.out.println("2. Player vs Computer");
            System.out.println("3. Exit");
            int choice = in.nextInt();

            if (choice == 1) playGame(false);
            else if (choice == 2) playGame(true);
            else {
                System.out.println("Thanks for playing!");
                break;
            }
        }
        in.close();
    }
}
