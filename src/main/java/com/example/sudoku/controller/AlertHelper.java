package com.example.sudoku.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Shows the dialogs used by the controllers: a confirmation that waits for the
 * player's answer and an information message.
 */
public class AlertHelper {

    private AlertHelper() {
    }

    /**
     * Asks the player to confirm an action and waits for the answer.
     *
     * @param title   title of the dialog
     * @param message question shown to the player
     * @return {@code true} when the player pressed OK
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows an information message and waits until the player closes it.
     *
     * @param title   title of the dialog
     * @param message text shown to the player
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
