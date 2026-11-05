package com.example.sudoku.model;

import java.util.List;

/**
 * Facade of the Sudoku model for the controllers.
 *
 * <p>Owns the current {@link SudokuBoard}, the {@link SudokuGenerator} that
 * builds each puzzle and the remaining hint counter. It exposes queries for
 * the view and commands that enforce the game rules.</p>
 *
 * <p>Belongs to the model layer: it has no dependency on JavaFX.</p>
 */
public class GameState {

    /** Number of hints the player gets per game. */
    public static final int MAX_HINTS = 3;

    private final SudokuBoard board;
    private final SudokuGenerator generator;
    private int hintsLeft;

    /**
     * Creates the game state with an empty board and a full hint counter.
     * Call {@link #newGame()} to generate the first puzzle.
     */
    public GameState() {
        this.board = new SudokuBoard();
        this.generator = new SudokuGenerator();
        this.hintsLeft = MAX_HINTS;
    }

    /** Starts a fresh puzzle and restores the hint counter. */
    public void newGame() {
        generator.generateNewGame(board);
        hintsLeft = MAX_HINTS;
    }

    /**
     * Returns the size of the board.
     *
     * @return The size of the board
     */
    public int getSize() {
        return SudokuBoard.SIZE;
    }

    /**
     * Returns the value at the specified row and column.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @return       stored value, {@code 0} when the cell is empty
     */
    public int getValue(int row, int column) {
        return board.getValue(row, column);
    }

    /**
     * Tells whether a cell is a fixed clue.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @return       {@code true} when the player cannot edit the cell
     */
    public boolean isClue(int row, int column) {
        return board.isClue(row, column);
    }

    /**
     * Returns the number of hints still available.
     *
     * @return remaining hints, between {@code 0} and {@link #MAX_HINTS}
     */
    public int getHintsLeft() {
        return hintsLeft;
    }

    /**
     * Finds the cells that would clash with a value at a given position.
     *
     * <p>The cell itself is excluded. Coordinates are returned as
     * {@code int[]{row, column}} and never repeated.</p>
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @param value  number to test, {@code 1..SIZE}; other values return an empty list
     * @return       the conflicting cells, empty when the placement is legal
     */
    public List<int[]> getConflicts(int row, int column, int value) {
        return board.getConflicts(row, column, value);
    }

    /**
     * The board is won when it is full and every cell respects the rules.
     *
     * @return true if the board is correctly filled
     */
    public boolean isWon() {
        return board.isComplete() && board.isValid();
    }

    /**
     * Stores a player value. Conflicts are allowed on purpose (HU-4).
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @param value  number to store, {@code 1..SIZE}
     * @return       true if the play was executed
     */
    public boolean play(int row, int column, int value) {
        if (board.isClue(row, column)) {
            return false;
        }
        if (value < 1 || value > SudokuBoard.SIZE) {
            return false;
        }
        board.setValue(row, column, value);
        return true;
    }

    /**
     * Empties a player cell; clue cells cannot be cleared.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @return       true if the cell was cleaned correctly
     */
    public boolean clearCell(int row, int column) {
        if (board.isClue(row, column)) {
            return false;
        }
        board.setValue(row, column, 0);
        return true;
    }

    /**
     * Fills one empty cell with the value that leaves it with the fewest
     * candidates and uses one hint.
     *
     * @return the filled cell as {@code int[]{row, column}}, or {@code null}
     *         when there are no hints left, the game is won or no legal
     *         value exists for any empty cell
     */
    public int[] giveHint() {
        if (hintsLeft == 0 || isWon()) {
            return null;
        }

        int bestRow = -1;
        int bestCol = -1;
        int bestValue = 0;
        int bestCount = Integer.MAX_VALUE;

        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                if (board.getValue(r, c) != 0) {
                    continue;
                }

                int count = 0;
                int firstValid = 0;
                for (int v = 1; v <= SudokuBoard.SIZE; v++) {
                    if (board.isValidPlacement(r, c, v)) {
                        count++;
                        if (firstValid == 0) {
                            firstValid = v;
                        }
                    }
                }

                if (count > 0 && count < bestCount) {
                    bestCount = count;
                    bestRow = r;
                    bestCol = c;
                    bestValue = firstValid;
                }
            }
        }

        // No empty cell can take a legal value (the board has conflicts)
        if (bestRow == -1) {
            return null;
        }

        board.setValue(bestRow, bestCol, bestValue);
        hintsLeft--;
        return new int[]{bestRow, bestCol};
    }
}
