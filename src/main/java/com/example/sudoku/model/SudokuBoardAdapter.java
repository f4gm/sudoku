package com.example.sudoku.model;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.function.BiConsumer;

public class SudokuBoardAdapter {
    public static void attachCellListeners(TextField textField, int row, int col, BiConsumer<Integer, Integer> onValueChanged) {


        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("[1-6]")) {
                event.consume();
            }
        });


        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                textField.clear();
                onValueChanged.accept(row, col);
            }
        });


        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.length() > 1) {
                textField.setText(newValue.substring(newValue.length() - 1));
            }
            onValueChanged.accept(row, col);
        });
    }
}

