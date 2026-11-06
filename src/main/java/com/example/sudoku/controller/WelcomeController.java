package com.example.sudoku.controller;

import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

/**
 * Welcome scene controller: starts a new game when the player clicks the start
 * button or presses ENTER anywhere in the scene.
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
     * Connects ENTER to the start action once the button belongs to a scene.
     * The button is not focusable, so ENTER is handled only once.
     */
    @FXML
    public void initialize() {
        gameButton.setFocusTraversable(false);
        gameButton.sceneProperty().addListener((observable, oldScene, scene) -> {
            if (scene != null) {
                scene.addEventHandler(KeyEvent.KEY_PRESSED, new EnterKeyAdapter(this::confirmAndStart));
            }
        });
    }

    /**
     * Handles the start button and navigates to the game scene.
     *
     * @param event the button press that triggered the action
     */
    @FXML
    private void onStart(ActionEvent event) {
        confirmAndStart();
    }

    /**
     * Asks for confirmation and, when accepted, navigates to the game scene.
     */
    private void confirmAndStart() {
        boolean confirmed = AlertHelper.showConfirmation(
                "Nuevo Juego",
                "¿Estás seguro de iniciar una nueva partida?"
        );

        if (confirmed) {
            navigator.showGame();
        }
    }
}
