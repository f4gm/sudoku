package com.example.sudoku;

import java.io.IOException;

import com.example.sudoku.service.navigation.Navigator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada del juego en JavaFX: crea el Navigator y muestra la
 * escena de bienvenida en el escenario principal.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Navigator navigator = new Navigator(stage);
        navigator.showWelcome();
    }

    public static void main(String[] args) {
        launch(args);
    }
}