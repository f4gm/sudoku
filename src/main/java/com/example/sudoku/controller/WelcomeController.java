package com.example.sudoku.controller;

import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.fxml.FXML;

/**
 * Controlador de la escena de bienvenida: inicia una nueva partida cuando el jugador
 * hace clic en el botón de inicio.
 */
public class WelcomeController implements Navigable {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    /**
     * Gestiona el botón de inicio y navega a la escena del juego.
     */
    @FXML
    private void onStart() {
        navigator.showGame();
    }
}