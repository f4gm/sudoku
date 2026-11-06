package com.example.sudoku.controller;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.function.BiConsumer;

/**
 * Adapter between the JavaFX text field of a cell and the controller. It keeps
 * only the digits 1 to 6, lets BACKSPACE and DELETE empty the cell, and reports
 * every change through a callback, so the controller does not depend on JavaFX
 * event details.
 */
public class SudokuBoardAdapter {

    private SudokuBoardAdapter() {
    }

    /**
     * Connects a cell to the keyboard and to the change callback.
     *
     * @param textField      the cell to connect
     * @param row            zero-based row index of the cell
     * @param col            zero-based column index of the cell
     * @param onValueChanged called with the cell position after every change
     */
    public static void attachCellListeners(TextField textField, int row, int col,
                                           BiConsumer<Integer, Integer> onValueChanged) {
        // Typed characters outside 1..6 are discarded before they reach the field
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("[1-6]")) {
                event.consume();
            }
        });

        // BACKSPACE and DELETE are consumed by the filter above, so they are handled here
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                textField.clear();
            }
        });

        // Any change, typed or pasted, is cleaned here and then reported once
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digits = newValue.replaceAll("[^1-6]", "");
            if (!digits.equals(newValue) || digits.length() > 1) {
                textField.setText(digits.isEmpty() ? "" : digits.substring(digits.length() - 1));
                return;
            }
            onValueChanged.accept(row, col);
        });
    }
}
