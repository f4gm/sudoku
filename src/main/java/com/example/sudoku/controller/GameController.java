package com.example.sudoku.controller;

import com.example.sudoku.model.AlertHelper;
import com.example.sudoku.model.GameState;
import com.example.sudoku.model.SudokuBoard;
import com.example.sudoku.model.SudokuBoardAdapter;
import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the game scene. Turns player actions into calls to
 * {@link GameState} and repaints the cells from the answers the model gives.
 * It holds no Sudoku rules of its own.
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

    private final GameState game;
    private final TextField[][] cellFields;

    /** Row of the cell the player last clicked, {@code -1} when none. */
    private int selectedRow = -1;

    /** Column of the cell the player last clicked, {@code -1} when none. */
    private int selectedCol = -1;

    /** Row of the cell filled by the last hint, {@code -1} when none. */
    private int hintRow = -1;

    /** Column of the cell filled by the last hint, {@code -1} when none. */
    private int hintCol = -1;

    /** Cells that currently repeat a number, recomputed after every edit. */
    private final List<int[]> conflictCells = new ArrayList<>();

    /**
     * {@code true} while the controller writes text into the cells itself.
     * Stops the text listener from treating that write as a keystroke.
     */
    private boolean syncingUI = false;

    public GameController() {
        this.game = new GameState();
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
                // Look and feel come from style.css: .celda + the block border class
                cell.getStyleClass().setAll("celda", blockBorderClass(r, c));

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

    /**
     * Picks the border class that draws the 2x3 block limits for a position.
     * Even rows open a block (thick top), odd rows close one (thick bottom);
     * columns 0 and 3 open a block (thick left), columns 2 and 5 close one
     * (thick right).
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @return one of {@code borde-sup}, {@code borde-sup-izq}, {@code borde-sup-der},
     *         {@code borde-inf}, {@code borde-inf-izq} or {@code borde-inf-der}
     */
    private String blockBorderClass(int row, int col) {
        String horizontal = (row % 2 == 0) ? "sup" : "inf";

        if (col % 3 == 0) {
            return "borde-" + horizontal + "-izq";
        }
        if (col % 3 == 2) {
            return "borde-" + horizontal + "-der";
        }
        return "borde-" + horizontal;
    }

    /** Starts a fresh puzzle and resets the view to match it. */
    private void startNewGame() {
        game.newGame();
        selectedRow = -1;
        selectedCol = -1;
        hintRow = -1;
        hintCol = -1;
        writeCellTexts();
        refreshConflicts();
        repaintAll();
        updateHelpButton();
        statusLabel.setText("¡Nuevo juego iniciado! Completa el tablero.");
    }

    /**
     * Asks for confirmation before discarding the current game.
     *
     * @param event the button press that triggered the action
     */
    @FXML
    void onNewGame(ActionEvent event) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Nuevo juego",
                "¿Descartar la partida actual y empezar una nueva?"
        );

        if (confirmed) {
            startNewGame();
        }
    }

    /**
     * Marks the clicked cell as the active one and gives it the keyboard focus.
     * Clues are skipped: the player cannot type on them.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     */
    private void selectCell(int row, int col) {
        if (game.isClue(row, col)) {
            statusLabel.setText("Esa casilla es una pista fija: no se puede editar.");
            return;
        }

        selectedRow = row;
        selectedCol = col;
        repaintAll();
        cellFields[row][col].requestFocus();
    }

    /**
     * Reacts to a change typed in a cell: stores it in the model, recomputes
     * the conflicts and repaints.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     */
    private void handleCellUpdate(int row, int col) {
        if (syncingUI) {
            return;
        }

        hintRow = -1;
        hintCol = -1;

        String text = cellFields[row][col].getText().trim();

        if (text.isEmpty()) {
            game.clearCell(row, col);
        } else if (!game.play(row, col, Integer.parseInt(text))) {
            return;
        }

        refreshConflicts();
        repaintAll();

        if (game.isWon()) {
            navigator.showGameOver("¡Felicidades, lograste superar el Sudoku!");
        } else if (!conflictCells.isEmpty()) {
            statusLabel.setText("Conflicto: hay números repetidos en fila, columna o bloque.");
        } else {
            statusLabel.setText(text.isEmpty() ? "Casilla vacía." : "Entrada válida.");
        }
    }

    /**
     * Fills one cell through the model and highlights it. The model decides
     * which cell and value; this method only shows the result.
     *
     * @param event the button press that triggered the action
     */
    @FXML
    void onHelp(ActionEvent event) {
        int[] cell = game.giveHint();

        if (cell == null) {
            statusLabel.setText("No hay ayudas disponibles en este momento.");
            updateHelpButton();
            return;
        }

        hintRow = cell[0];
        hintCol = cell[1];
        writeCellTexts();
        refreshConflicts();
        repaintAll();
        updateHelpButton();
        statusLabel.setText("Ayuda: la casilla resaltada ahora tiene el número "
                + game.getValue(hintRow, hintCol) + ".");
    }

    /** Shows how many hints are left and disables the button when none are. */
    private void updateHelpButton() {
        helpButton.setText("Ayuda (" + game.getHintsLeft() + ")");
        helpButton.setDisable(game.getHintsLeft() == 0);
    }

    /**
     * Copies the model's values into the text fields. Clues become read-only.
     * The flag stops the text listener from reading back what was just written.
     */
    private void writeCellTexts() {
        syncingUI = true;
        try {
            for (int r = 0; r < SudokuBoard.SIZE; r++) {
                for (int c = 0; c < SudokuBoard.SIZE; c++) {
                    int value = game.getValue(r, c);
                    cellFields[r][c].setText(value == 0 ? "" : String.valueOf(value));
                    cellFields[r][c].setEditable(!game.isClue(r, c));
                }
            }
        } finally {
            syncingUI = false;
        }
    }

    /**
     * Recomputes every cell that repeats a number with another one, so a
     * conflict disappears as soon as the repeated number is corrected.
     */
    private void refreshConflicts() {
        conflictCells.clear();

        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                int value = game.getValue(r, c);
                if (value != 0 && !game.getConflicts(r, c, value).isEmpty()) {
                    conflictCells.add(new int[]{r, c});
                }
            }
        }
    }

    /**
     * Repaints every cell from a single source of truth: the block border comes
     * from its position and the state class from what the cell currently is.
     */
    private void repaintAll() {
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                resetCellBorder(cellFields[r][c], r, c);
            }
        }
    }

    /**
     * Rebuilds the style class list of one cell. The list always holds the base
     * class plus the block border, and at most one state class on top of them.
     *
     * @param cell the cell to restyle
     * @param row  zero-based row index
     * @param col  zero-based column index
     */
    private void resetCellBorder(TextField cell, int row, int col) {
        String state = stateClass(row, col);

        if (state == null) {
            cell.getStyleClass().setAll("celda", blockBorderClass(row, col));
        } else {
            cell.getStyleClass().setAll("celda", blockBorderClass(row, col), state);
        }
    }

    /**
     * Resolves the state class of a cell, if any. Priority: an immutable clue
     * never looks like an error, and an error outranks the help and selection.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @return the state class, or {@code null} when the cell is at rest
     */
    private String stateClass(int row, int col) {
        if (game.isClue(row, col)) {
            return "celda-pista";
        }
        if (isConflict(row, col)) {
            return "celda-error";
        }
        if (row == hintRow && col == hintCol) {
            return "celda-ayuda";
        }
        if (row == selectedRow && col == selectedCol) {
            return "celda-seleccionada";
        }
        return null;
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
