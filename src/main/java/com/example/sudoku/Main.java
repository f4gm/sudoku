package com.example.sudoku;

import java.io.IOException;

import com.example.sudoku.service.navigation.Navigator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX game entry point: creates the Navigator and displays the
 * welcome scene on the main stage.
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