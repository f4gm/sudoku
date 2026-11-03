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
    private Label feedbackLabel;

    @FXML
    private Label recordLabel;

    @FXML
    private Label remainingTimeLabel;

    /**
     * Restarts from level 1
     */
    @FXML
    private void restartGame() {
        navigator.showGame();
    }

    /**
     * Draws the game summary.
     * Executed after initialize(), so the labels have already been injected.
     */
    @Override
    public void setSummary(int completedLevels, String reason, double remainingSeconds) {
        feedbackLabel.setText(reason);
        recordLabel.setText("Niveles completados: " + completedLevels);
        remainingTimeLabel.setText("Tiempo restante: " + remainingSeconds + "s");
    }
}
