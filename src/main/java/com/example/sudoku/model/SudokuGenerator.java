package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Generates random 6x6 Sudoku puzzles using a Backtracking algorithm.
 *
 * <p>The generator first creates a complete and valid Sudoku solution
 * by recursively filling empty cells with randomly ordered candidate
 * numbers. Once the solution has been found, a fixed number of clues
 * is kept per 2×3 block (see {@link #CLUES_PER_BLOCK}) and the rest are
 * emptied to create the initial puzzle.</p>
 *
 * <p>Remaining filled cells are marked as fixed clues, preventing
 * the player from modifying them during the game.</p>
 *
 * <p>The generated puzzle is guaranteed to have at least one solution,
 * since it is derived from a previously completed valid board.
 * However, uniqueness of the solution and puzzle difficulty are
 * not guaranteed.</p>
 *
 * <p>This class belongs to the model layer and has no dependency
 * on JavaFX.</p>
 */
public class SudokuGenerator {
    private final Random random;

    /** Clues kept per 2x3 block; with 6 blocks the puzzle starts with 12 clues. */
    private static final int CLUES_PER_BLOCK = 2;

    public SudokuGenerator() {
        this.random = new Random();
    }

    /**
     * Generates a new Sudoku game.
     * The generated board always has at least one solution.
     *
     * @param board board to initialize
     */
    public void generateNewGame(SudokuBoard board) {
        board.clear();

        // Generate a complete and valid Sudoku solution
        if (!fillBoard(board)) {
            throw new IllegalStateException(
                    "Unable to generate a valid Sudoku board."
            );
        }

        // Print the solution to the console before starting the game
        board.printBoard();

        // Remove numbers to create the puzzle
        keepCluesPerBlock(board);

        // Mark remaining numbers as fixed clues
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int col = 0; col < SudokuBoard.SIZE; col++) {
                if (board.getValue(row, col) != 0) {
                    board.setInitial(row, col, true);
                }
            }
        }
    }

    /**
     * Fills the board using Backtracking.
     *
     * @param board Sudoku board
     * @return true if a complete solution is found
     */
    private boolean fillBoard(SudokuBoard board) {
        // Find the next empty list by traversing the entire board.
        // The coordinates remain -1 if no empty cell is found.
        int row = -1;
        int col = -1;

        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                if (board.getValue(r, c) == 0) {
                    row = r;
                    col = c;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }

        // Base case: if there are no empty cells, the board is complete.
        // Returning true indicates that a valid solution has been found.
        if (row == -1) {
            return true;
        }

        // Create a list containing all possible numbers for an empty cell.
        List<Integer> numbers = new ArrayList<>(
                List.of(1, 2, 3, 4, 5, 6)
        );

        // Shuffle the candidates to introduce randomness in the generated board.
        Collections.shuffle(numbers, random);

        // Try each candidate until a valid solution is found.
        for (int number : numbers) {

            // Check whether the candidate respects the Sudoku rules
            // for the current row, column, and subgrid.
            if (board.isValidPlacement(row, col, number)) {

                // Place the candidate in the current empty cell.
                board.setValue(row, col, number);

                // Recursive step: call the same method to fill the next empty cell.
                // If the recursive call returns true, the solution is complete,
                // so propagate true through all previous recursive calls.
                if (fillBoard(board)) {
                    return true;
                }

                // Backtracking: the candidate did not lead to a complete solution.
                // Remove it and restore the cell to its original empty state
                // so another candidate can be tested.
                board.setValue(row, col, 0);
            }
        }

        // All candidates have been tested, but none produced a valid solution.
        // Return false to indicate that the previous recursive call must backtrack.
        return false;
    }

    /**
     * Keeps a fixed number of clues per 2x3 block and empties the remaining cells.
     *
     * @param board completed Sudoku board
     */
    private void keepCluesPerBlock(SudokuBoard board) {
        // Number of vertical blocks
        int blockRows = SudokuBoard.SIZE / SudokuBoard.SUBGRID_ROWS; // 3
        // Number of horizontal blocks
        int blockCols = SudokuBoard.SIZE / SudokuBoard.SUBGRID_COLS; // 2

        // Each block is iterated through using a double for loop
        for (int blockRow = 0; blockRow < blockRows; blockRow++) {
            for (int blockCol = 0; blockCol < blockCols; blockCol++) {
                // Coordinates of the first cell of the block
                int startRow = blockRow * SudokuBoard.SUBGRID_ROWS;
                int startCol = blockCol * SudokuBoard.SUBGRID_COLS;

                // The cell coordinates are added to a list
                List<int[]> cells = new ArrayList<>();
                for (int r = startRow; r < startRow + SudokuBoard.SUBGRID_ROWS; r++) {
                    for (int c = startCol; c < startCol + SudokuBoard.SUBGRID_COLS; c++) {
                        cells.add(new int[]{r, c});
                    }
                }

                // The list is randomly shuffled, and the value of any cells > `CLUES PER_BLOCK` limit is set to 0 (empty)
                Collections.shuffle(cells, random);
                for (int i = CLUES_PER_BLOCK; i < cells.size(); i++) {
                    board.setValue(cells.get(i)[0], cells.get(i)[1], 0);
                }
            }
        }
    }
}
