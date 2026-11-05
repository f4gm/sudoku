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
 * numbers. Once the solution has been found, exactly 2 clues are kept
 * per 2×3 block (12 clues in total) and the rest are emptied to create
 * the initial puzzle.</p>
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

        // Remove numbers to create the puzzle
        keepTwoCluesPerBlock(board);

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

        // Find the next empty cell
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

        // No empty cells: solution found
        if (row == -1) {
            return true;
        }

        // Generate candidate numbers in random order
        List<Integer> numbers = new ArrayList<>(
                List.of(1, 2, 3, 4, 5, 6)
        );

        Collections.shuffle(numbers, random);

        // Try each candidate
        for (int number : numbers) {
            if (board.isValidPlacement(row, col, number)) {
                board.setValue(row, col, number);
                if (fillBoard(board)) {
                    return true;
                }

                // Backtrack
                board.setValue(row, col, 0);
            }
        }

        // No candidate produced a valid solution
        return false;
    }

    /**
     * Keeps exactly two clues per block and empties the remaining cells.
     *
     * @param board completed Sudoku board
     */
    private void keepTwoCluesPerBlock(SudokuBoard board) {
        int blockRows = SudokuBoard.SIZE / SudokuBoard.SUBGRID_ROWS; // 3
        int blockCols = SudokuBoard.SIZE / SudokuBoard.SUBGRID_COLS; // 2

        for (int blockRow = 0; blockRow < blockRows; blockRow++) {
            for (int blockCol = 0; blockCol < blockCols; blockCol++) {
                int startRow = blockRow * SudokuBoard.SUBGRID_ROWS;
                int startCol = blockCol * SudokuBoard.SUBGRID_COLS;

                List<int[]> cells = new ArrayList<>();
                for (int r = startRow; r < startRow + SudokuBoard.SUBGRID_ROWS; r++) {
                    for (int c = startCol; c < startCol + SudokuBoard.SUBGRID_COLS; c++) {
                        cells.add(new int[]{r, c});
                    }
                }

                Collections.shuffle(cells, random);
                for (int i = 2; i < cells.size(); i++) {
                    board.setValue(cells.get(i)[0], cells.get(i)[1], 0);
                }
            }
        }
    }
}
