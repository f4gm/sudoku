package com.example.sudoku.controller;

import com.example.sudoku.model.GameState;
import com.example.sudoku.service.timer.Timer;
import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;

/**
 * Controlador para la escena del juego. Conecta el modelo {@link GameState} con
 * la vista, gestiona el {@link Timer} del nivel.
 */
public class GameController implements Navigable {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
