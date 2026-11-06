package com.example.sudoku.controller;

import com.example.sudoku.model.AlertHelper;
import com.example.sudoku.model.GameState;
import com.example.sudoku.model.SudokuBoard;
import com.example.sudoku.model.SudokuBoardAdapter;
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
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
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

    /** Row of the cell the player last clicked, {@code -1} when none. */
    private int selectedRow = -1;

    /** Column of the cell the player last clicked, {@code -1} when none. */
    private int selectedCol = -1;

    /**
     * Cells currently painted red because they repeat a number. Kept so the
     * next repaint can clear them without scanning the whole board.
     */
    private final List<int[]> conflictCells = new ArrayList<>();

    /**
     * {@code true} while the controller writes into the cells on purpose.
     * Guards the text listener from treating a programmatic fill as a keystroke.
     */
    private boolean syncingUI = false;

    public GameController() {
        this.board = new SudokuBoard();
        this.generator = new SudokuGenerator();
        this.cellFields = new TextField[SudokuBoard.SIZE][SudokuBoard.SIZE];
    }
    @FXML
    public void initialize() {
        buildgridUI();
        startNewGame();
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

                // Mouse: selecting the cell is what gives it focus for typing
                cell.setOnMouseClicked(event -> selectCell(row, col));

                // Keyboard: filters 1..6, handles BACKSPACE/DELETE and reports changes
                SudokuBoardAdapter.attachCellListeners(cell, row, col, this::handleCellUpdate);

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

    /**
     * Marks the clicked cell as the active one and gives it the keyboard focus.
     * Clues are skipped: the player cannot type on them.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     */
    private void selectCell(int row, int col) {
        if (board.isClue(row, col)) {
            statusLabel.setText("Esa casilla es una pista fija: no se puede editar.");
            return;
        }

        int previousRow = selectedRow;
        int previousCol = selectedCol;

        selectedRow = row;
        selectedCol = col;

        if (previousRow >= 0) {
            resetCellBorder(cellFields[previousRow][previousCol], previousRow, previousCol);
        }
        repaintSelection();
        cellFields[row][col].requestFocus();
    }

    /** Paints the active cell with the selection colour. */
    private void repaintSelection() {
        if (selectedRow < 0) {
            return;
        }
        cellFields[selectedRow][selectedCol].setStyle(
                "-fx-border-color: #3b82f6; -fx-border-width: 3px;"
                        + " -fx-background-color: #dbeafe; -fx-text-fill: #1e293b;"
                        + " -fx-alignment: center;"
        );
    }

    private void handleCellUpdate(int row, int col) {
        if (syncingUI) {
            return;
        }
        TextField cell = cellFields[row][col];
        String text = cell.getText().trim();

        if (text.isEmpty()) {
            board.setValue(row, col, 0);
            conflictCells.clear();
            repaintAll();
            statusLabel.setText("Casilla vacía.");
        } else {
            int value = Integer.parseInt(text);
            board.setValue(row, col, value);

            conflictCells.clear();
            conflictCells.addAll(board.getConflicts(row, col, value));
            repaintAll();

            if (!conflictCells.isEmpty()) {
                statusLabel.setText("Conflicto: El número " + value
                        + " se repite en fila, columna o bloque.");
            } else {
                statusLabel.setText("Entrada válida.");

                // Verificar si se completó el juego
                if (board.isComplete() && board.isValid()) {
                    statusLabel.setText("¡Felicidades! Has resuelto el Sudoku con éxito.");
                    AlertHelper.showInfo("¡Juego Completado!",
                            "¡Excelente trabajo! Has completado el tablero.");
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
     * Sincroniza la vista con el estado del modelo tras generar un nuevo juego.
     * The flag stops the text listener from reading back what we just wrote.
     */
    private void updateUIFromBoard() {
        syncingUI = true;
        try {
            for (int r = 0; r < SudokuBoard.SIZE; r++) {
                for (int c = 0; c < SudokuBoard.SIZE; c++) {
                    TextField cell = cellFields[r][c];
                    int val = board.getValue(r, c);

                    if (board.isClue(r, c)) {
                        cell.setText(String.valueOf(val));
                        cell.setEditable(false);
                    } else {
                        cell.setText(val == 0 ? "" : String.valueOf(val));
                        cell.setEditable(true);
                    }
                }
            }
        } finally {
            syncingUI = false;
        }

        selectedRow = -1;
        selectedCol = -1;
        conflictCells.clear();
        repaintAll();
    }

    /**
     * Repaints every cell from a single source of truth: clue colour wins first,
     * then conflict red, then the blue selection border.
     */
    private void repaintAll() {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                resetCellBorder(cellFields[r][c], r, c);
            }
        }
    }

    /** Gives one cell the style dictated by its state. */
    private void resetCellBorder(TextField cell, int row, int col) {
        if (board.isClue(row, col)) {
            cell.setStyle("-fx-background-color: #e2e8f0; -fx-font-weight: bold;"
                    + " -fx-text-fill: black; -fx-alignment: center;");
            return;
        }

        if (isConflict(row, col)) {
            cell.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2px;"
                    + " -fx-background-color: #fee2e2; -fx-text-fill: #991b1b;"
                    + " -fx-alignment: center;");
            return;
        }

        if (row == selectedRow && col == selectedCol) {
            cell.setStyle("-fx-border-color: #3b82f6; -fx-border-width: 3px;"
                    + " -fx-background-color: #dbeafe; -fx-text-fill: #1e293b;"
                    + " -fx-alignment: center;");
            return;
        }

        cell.setStyle("-fx-border-color: #cbd5e1; -fx-background-color: white;"
                + " -fx-text-fill: #1e293b; -fx-alignment: center;");
    }

    /**
     * @param row zero-based row index
     * @param col zero-based column index
     * @return {@code true} when the cell repeats a number with another one
     */
    private boolean isConflict(int row, int col) {
        for (int[] pos : conflictCells) {
            if (pos[0] == row && pos[1] == col) {
                return true;
            }
        }
        return false;
    }
}







