package com.example.sudoku.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Adapter class for keyboard events. Validates the
 * response when the player presses ENTER, ignoring any other key.
 */
public class EnterKeyAdapter implements EventHandler<KeyEvent> {

    private final Runnable action;

    /**
     * @param action what to execute when ENTER is pressed
     */
    public EnterKeyAdapter(Runnable action) {
        this.action = action;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED
                && event.getCode() == KeyCode.ENTER) {
            action.run();
        }
    }
}
