package com.example.sudoku.controller;

import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import com.example.sudoku.service.navigation.SummaryAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador para la escena de fin de partida: dibuja el resumen del encuentro
 * proporcionado por el Navegador y reinicia el juego.
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
     * Reinicia desde el nivel 1
     */
    @FXML
    private void restartGame() {
        navigator.showGame();
    }

    /**
     * Dibuja el resumen de la partida.
     * Se ejecuta después de initialize(), por lo que las etiquetas ya han sido inyectadas.
     */
    @Override
    public void setSummary(int completedLevels, String reason, double remainingSeconds) {
        feedbackLabel.setText(reason);
        recordLabel.setText("Niveles completados: " + completedLevels);
        remainingTimeLabel.setText("Tiempo restante: " + remainingSeconds + "s");
    }
}
