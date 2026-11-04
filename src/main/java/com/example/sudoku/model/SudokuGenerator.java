package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private final Random random;

    public SudokuGenerator() {
        this.random = new Random();
    }

    public void generateNewGame(SudokuBoard board) {
        board.clear();

        // El tablero de 6x6 se divide en 3 filas y 2 columnas de bloques de 2x3
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 2; blockCol++) {
                int startRow = blockRow * SudokuBoard.SUBGRID_ROWS;
                int startCol = blockCol * SudokuBoard.SUBGRID_COLS;

                placeTwoRandomNumbersInBlock(board, startRow, startCol);
            }
        }
    }

    private void placeTwoRandomNumbersInBlock(SudokuBoard board, int startRow, int startCol) {
        // Obtenemos las 6 coordenadas [fila, columna] internas de este bloque de 2x3
        List<int[]> blockCells = new ArrayList<>();
        for (int r = startRow; r < startRow + SudokuBoard.SUBGRID_ROWS; r++) {
            for (int c = startCol; c < startCol + SudokuBoard.SUBGRID_COLS; c++) {
                blockCells.add(new int[]{r, c});
            }
        }

        // Desordenamos aleatoriamente las posiciones y las cifras disponibles (1 a 6)
        Collections.shuffle(blockCells, random);
        List<Integer> possibleNumbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Collections.shuffle(possibleNumbers, random);

        int placedCount = 0;
        for (int[] cell : blockCells) {
            if (placedCount == 2) {
                break; // Garantiza exactamente 2 números por bloque
            }

            int row = cell[0];
            int col = cell[1];

            for (int num : possibleNumbers) {
                if (board.isValidPlacement(row, col, num)) {
                    board.setValue(row, col, num);
                    board.setInitial(row, col, true); // Lo marca como fijo (editable = false)
                    possibleNumbers.remove(Integer.valueOf(num));
                    placedCount++;
                    break;
                }
            }
        }
    }
}
