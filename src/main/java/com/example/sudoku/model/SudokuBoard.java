package com.example.sudoku.model;

/**
 * Model of a 6x6 Sudoku board: the game rules and nothing else, with no dependency
 * on JavaFX. A value of {@code 0} means an empty cell, {@code 1..6} are playable numbers.
 *
 * <p>Invariants: values stay in {@code 0..SIZE}, both grids keep the same shape, and
 * clue cells are never editable by the player.</p>
 */
public class SudokuBoard {

    /** Board side, also the highest playable number. */
    private static final int SIZE = 6;

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
    public boolean isValidPlacement(int row, int column, int value){
        if (value == 0) {
            return true;
        }
        if (value < 1 || value > SIZE) {
            return false;
        }

        for (int i = 0; i < SIZE; i++) {
            if (i != column && values[row][i] == value) {
                return false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (i != row && values[i][column] == value) {
                return false;
            }
        }

        int startRow = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int startCol = (column / SUBGRID_COLS) * SUBGRID_COLS;
        for (int r = startRow; r < startRow + SUBGRID_ROWS; r++) {
            for (int c = startCol; c < startCol + SUBGRID_COLS; c++) {
                if ((r != row || c != column) && values[r][c] == value) {
                    return false;
                }
            }
        }

        return true;
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



}
