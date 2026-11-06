package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model of a 6x6 Sudoku board: the game rules and nothing else, with no dependency
 * on JavaFX. A value of {@code 0} means an empty cell, {@code 1..6} are playable numbers.
 *
 * <p>Invariants: values stay in {@code 0..SIZE}, both grids keep the same shape, and
 * clue cells are never editable by the player.</p>
 */
public class SudokuBoard {

    /** Board side, also the highest playable number. */
    public static final int SIZE = 6;

    /** Height of a 2x3 block: two rows. */
    public static final int SUBGRID_ROWS = 2;

    /** Width of a 2x3 block: three columns. */
    public static final int SUBGRID_COLS = 3;

    /** {@code 0} = empty cell, {@code 1..6} = playable number. */
    private final int[][] values;

    /** {@code true} when the cell is a fixed clue the player cannot edit. */
    private final boolean[][] isClue;

    /**
     * Creates an empty board where every cell is editable. Allocating both grids here
     * is what keeps them aligned.
     */
    public SudokuBoard(){
        this.values=new int[SIZE][SIZE];
        this.isClue=new boolean[SIZE][SIZE];
    }

    /**
     * Reads the number stored in a cell.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @return       stored value, {@code 0} when the cell is empty
     */
    public int getValue(int row, int column){
        return values[row][column];
    }

    /**
     * Print the board to the console
     */
    public void printBoard() {
        System.out.println(
                Arrays.deepToString(values)
        );
    }

    /**
     * Tells whether a cell is a fixed clue.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @return       {@code true} when the player cannot edit the cell
     */
    public boolean isClue(int row, int column){
        return isClue[row][column];
    }


    /**
     * Writes a number into a cell; the call is ignored when the cell is a clue or the
     * value is outside {@code 0..SIZE}. Passing {@code 0} clears the cell.
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @param value  number to store, {@code 0..SIZE}
     */
    public void setValue(int row, int column, int value){
        if (isClue[row][column]) {
            return;
        }
        if (value < 0 || value > SIZE) {
            return;
        }
        values[row][column] = value;
    }

    /**
     * Flags a cell as a clue or releases it, so the initial puzzle can be built.
     *
     * <p><b>Package-private on purpose:</b> only classes in this package may call it,
     * which keeps clue cells out of the UI's reach. Set the value <i>before</i> flagging,
     * since {@link #setValue} refuses to write on a clue.</p>
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @param clue   {@code true} to freeze the cell, {@code false} to release it
     */
    void setInitial(int row, int column, boolean clue){
        isClue[row][column] = clue;
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
        List<int[]> conflicts = new ArrayList<>();

        // Out-of-range values can never conflict
        if (value < 1 || value > SIZE) {
            return conflicts;
        }

        boolean[][] marked = new boolean[SIZE][SIZE];

        // Same row
        for (int c = 0; c < SIZE; c++) {
            if (c != column && values[row][c] == value) {
                addConflict(conflicts, marked, row, c);
            }
        }

        // Same column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && values[r][column] == value) {
                addConflict(conflicts, marked, r, column);
            }
        }

        // Same 2x3 block
        int startRow = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int startCol = (column / SUBGRID_COLS) * SUBGRID_COLS;
        for (int r = startRow; r < startRow + SUBGRID_ROWS; r++) {
            for (int c = startCol; c < startCol + SUBGRID_COLS; c++) {
                if ((r != row || c != column) && values[r][c] == value) {
                    addConflict(conflicts, marked, r, c);
                }
            }
        }

        return conflicts;
    }

    /** Adds a coordinate only the first time it is found. */
    private void addConflict(List<int[]> conflicts, boolean[][] marked,
                             int row, int column) {
        if (!marked[row][column]) {
            marked[row][column] = true;
            conflicts.add(new int[]{row, column});
        }
    }

    /**
     * Checks whether a number may legally occupy a cell: it must not repeat in its row,
     * column or 2x3 block. The cell itself is excluded from all three checks, so a value
     * already stored there does not conflict with itself.
     *
     * <p>Answers the Sudoku rules only; whether the cell is a clue is
     * {@link #setValue}'s business.</p>
     *
     * @param row    zero-based row index
     * @param column zero-based column index
     * @param value  number to test, {@code 0..SIZE}
     * @return       {@code true} when the number breaks no rule at this position
     */
    public boolean isValidPlacement(int row, int column, int value) {
        // Clearing a cell is always allowed
        if (value == 0) {
            return true;
        }

        // Invalid value
        if (value < 1 || value > SIZE) {
            return false;
        }

        // The rules live in getConflicts: no conflicts means a legal placement
        return getConflicts(row, column, value).isEmpty();
    }

    /**
     * Empties the board, leaving every cell editable.
     */
    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                values[r][c] = 0;
                isClue[r][c] = false;
            }
        }
    }

    /**
     * Tells whether every cell holds a number.
     *
     * @return {@code true} when no cell is empty
     */
    public boolean isComplete() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (values[r][c] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks the whole board against the Sudoku rules.
     *
     * @return {@code true} when no filled cell breaks a row, column or block rule
     */
    public boolean isValid() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (values[r][c] != 0 && !isValidPlacement(r, c, values[r][c])) {
                    return false;
                }
            }
        }
        return true;
    }
}
