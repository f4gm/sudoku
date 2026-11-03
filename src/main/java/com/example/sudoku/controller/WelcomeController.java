package com.example.sudoku.controller;

import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.fxml.FXML;

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

    /**
     * Handles the start button and navigates to the game scene.
     */
    @FXML
    private void onStart() {
        navigator.showGame();
    }
}