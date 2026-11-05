package com.example.sudoku.controller;

import com.example.sudoku.model.AlertHelper;
import com.example.sudoku.model.GameState;
import com.example.sudoku.model.SudokuBoard;
import com.example.sudoku.model.SudokuGenerator;
import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.event.ActionEvent;
import java.util.List;


/**
 * Controller for the game scene. Connects the {@link GameState} model with
 * the view.
 */
public class GameController implements Navigable {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Button helpButton;

    @FXML
    private Label statusLabel;

    private final SudokuBoard board;
    private final SudokuGenerator generator;
    private TextField[][] cellFields;

    public GameController() {
        this.board = new SudokuBoard();
        this.generator = new SudokuGenerator();
        this.cellFields = new TextField[SudokuBoard.SIZE][SudokuBoard.SIZE];
    }
    @FXML
    public void initialize() {
        buildgridUI();
    }

    private void buildgridUI() {
        sudokuGrid.getChildren().clear();

        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setAlignment(Pos.CENTER);
                cell.setFont(Font.font("System", FontWeight.BOLD, 18));

                final int row = r;
                final int col = c;

                cellFields[r][c] = cell;
                sudokuGrid.add(cell, c, r);
            }
        }
    }

    private void startNewGame() {
        generator.generateNewGame(board);
        updateUIFromBoard();
        statusLabel.setText("¡Nuevo juego iniciado! Completa el tablero.");
    }

    private void handleCellUpdate(int row, int col) {
        TextField cell = cellFields[row][col];
        String text = cell.getText().trim();

        if (text.isEmpty()) {
            board.setValue(row, col, 0);
            clearHighlights();
            statusLabel.setText("Casilla vacía.");
        } else {
            int value = Integer.parseInt(text);
            board.setValue(row, col, value);

            clearHighlights();

            // Usamos getConflicts del código de tus compañeros para detectar y marcar colisiones
            List<int[]> conflicts = board.getConflicts(row, col, value);

            if (!conflicts.isEmpty()) {
                // Marcar celda actual en rojo
                cell.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-text-fill: red;");

                // Marcar celdas en conflicto
                for (int[] pos : conflicts) {
                    int cr = pos[0];
                    int cc = pos[1];
                    cellFields[cr][cc].setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: #fee2e2;");
                }

                statusLabel.setText("Conflicto: El número " + value + " se repite en fila, columna o bloque.");
            } else {
                statusLabel.setText("Entrada válida.");

                // Verificar si se completó el juego
                if (board.isComplete() && board.isValid()) {
                    statusLabel.setText("¡Felicidades! Has resuelto el Sudoku con éxito.");
                    AlertHelper.showInfo("¡Juego Completado!", "¡Excelente trabajo! Has completado el tablero.");
                }
            }
        }
    }

    @FXML
    void onHelp(ActionEvent event) {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                if (board.getValue(r, c) == 0) {
                    for (int num = 1; num <= SudokuBoard.SIZE; num++) {
                        if (board.isValidPlacement(r, c, num)) {
                            cellFields[r][c].setStyle("-fx-border-color: #3b82f6; -fx-border-width: 2px; -fx-background-color: #dbeafe;");
                            statusLabel.setText("Sugerencia: Intenta colocar el número " + num + " en la celda resaltada.");
                            return;
                        }
                    }
                }
            }
        }
        statusLabel.setText("No hay casillas vacías disponibles para dar sugerencias.");
    }

    /**
     * Sincroniza la vista con el estado del modelo tras generar un nuevo juego[cite: 1, 5].
     */
    private void updateUIFromBoard() {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                TextField cell = cellFields[r][c];
                int val = board.getValue(r, c);

                if (board.isClue(r, c)) { // Adaptado a isClue() del modelo de tus compañeros
                    cell.setText(String.valueOf(val));
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: #e2e8f0; -fx-font-weight: bold; -fx-alignment: center;");
                } else {
                    cell.setText(val == 0 ? "" : String.valueOf(val));
                    cell.setEditable(true);
                    resetCellBorder(cell, r, c);
                }
            }
        }
    }

    /**
     * Restablece los estilos visuales estándar de las celdas[cite: 1, 5].
     */
    private void clearHighlights() {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                resetCellBorder(cellFields[r][c], r, c);
            }
        }
    }

    private void resetCellBorder(TextField cell, int row, int col) {
        if (board.isClue(row, col)) { // Usando el método del modelo
            cell.setStyle("-fx-background-color: #e2e8f0; -fx-font-weight: bold; -fx-text-fill: black; -fx-alignment: center;");
        } else {
            cell.setStyle("-fx-border-color: #cbd5e1; -fx-background-color: white; -fx-text-fill: #1e293b; -fx-alignment: center;");
        }
    }
}







