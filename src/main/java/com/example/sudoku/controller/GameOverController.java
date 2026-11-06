package com.example.sudoku.controller;

import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import com.example.sudoku.service.navigation.SummaryAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the game-over scene: renders the match summary
 * provided by the Navigator and restarts the game.
 */
public class GameOverController implements Navigable, SummaryAware {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private Label messageLabel;

    /**
     * Starts a new game and returns to the game scene.
     */
    @FXML
    private void restartGame() {
        navigator.showGame();
    }

    /**
     * Shows the message. The Navigator calls this after the FXML is loaded,
     * so {@code messageLabel} has already been injected.
     *
     * @param message text shown on the game-over screen
     */
    @Override
    public void setSummary(String message) {
        messageLabel.setText(message);
    }
}
