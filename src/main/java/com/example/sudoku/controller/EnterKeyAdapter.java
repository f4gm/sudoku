package com.example.sudoku.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Clase adaptadora para eventos de teclado. Valida la
 * respuesta cuando el jugador pulsa INTRO, ignorando cualquier otra tecla.
 */
public class EnterKeyAdapter implements EventHandler<KeyEvent> {

    private final Runnable action;

    /**
     * @param action qué ejecutar al pulsar INTRO
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
