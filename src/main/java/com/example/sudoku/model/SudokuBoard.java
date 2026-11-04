package com.example.sudoku.model;

public class SudokuBoard {
    public static final int SIZE = 6;
    public static final int SUBGRID_ROWS = 2;
    public static final int SUBGRID_COLS = 3;

    private final int[][] grid;
    private final boolean[][] initialMask;

    public SudokuBoard() {
        this.grid = new int [SIZE][SIZE];
        this.initialMask = new boolean [SIZE][SIZE];
    }

    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
                initialMask[r][c] = false;
            }
        }
    }

    public void setValue(int row, int col, int value) {
        if (isValidPosition(row, col)) {
            grid[row][col] = value;
        }
    }

    public int getValue(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        return 0;
    }

    public void setInitial(int row, int col, boolean isInitial) {
        if (isValidPosition(row, col)) {
            initialMask[row][col] = isInitial;
        }
    }

    public boolean isInitial(int row, int col) {
        if (isValidPosition(row, col)) {
            return initialMask[row][col];
        }
        return false;
    }

    public boolean isValidPlacement(int row, int col, int num) {
        if (num < 1 || num > 6) {
            return false;
        }

        for (int c = 0; c < SIZE; c++) {
            if (c != col && grid[row][c] == num) {
                return false;
            }
        }

        for (int r = 0; r < SIZE; r++) {
            if (r != row && grid[r][col] == num) {
                return false;
            }
        }
        int startRow = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int startCol = (col / SUBGRID_COLS) * SUBGRID_COLS;

        for (int r = startRow; r < startRow + SUBGRID_ROWS; r++) {
            for (int c = startCol; c < startCol + SUBGRID_COLS; c++) {
                if ((r != row || c != col) && grid[r][c] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isCompleted() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int val = grid[r][c];
                if (val == 0 || !isValidPlacement(r, c, val)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
