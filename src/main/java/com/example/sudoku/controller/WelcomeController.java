package com.example.sudoku.controller;

import com.example.sudoku.model.AlertHelper;
import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Welcome scene controller: starts a new game when the player
 * clicks the start button.
 */
public class WelcomeController implements Navigable {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private Button gameButton;

    /**
     * Handles the start button and navigates to the game scene.
     */
    @FXML
    private void onStart(ActionEvent event) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Nuevo Juego",
                "¿Estás seguro de iniciar una nueva partida?"
        );

        if (confirmed) {
            navigator.showGame();
        }
    }
}