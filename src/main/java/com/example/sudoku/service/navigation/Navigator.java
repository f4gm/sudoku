package com.example.sudoku.service.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Switches the main window between the three game scenes. After
 * loading an FXML file, it injects data into {@link Navigable}
 * controllers and provides additional data to {@link SummaryAware}
 * controllers.
 */
public class Navigator {
    private final Stage stage;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    /**
     * Displays the welcome scene.
     */
    public void showWelcome() {
        show("/com/example/sudoku/view/Welcome.fxml");
    }

    /**
     * Displays the game scene with an all-new level 1.
     */
    public void showGame() {
        show("/com/example/sudoku/view/Game.fxml");
    }

    /**
     * Displays the game-over screen and presents a summary of the game.
     *
     * @param completedLevels levels completed by the player
     * @param reason the reason the game ended
     * @param remainingSeconds seconds remaining when the game ended
     */
    public void showGameOver(int completedLevels, String reason, double remainingSeconds) {
        Object controller = show("/com/example/sudoku/view/GameOver.fxml");

        if (controller instanceof SummaryAware summaryAware) {
            summaryAware.setSummary(completedLevels, reason, remainingSeconds);
        }
    }

    /**
     * Loads an FXML file, links the controller, and displays it on the stage.
     *
     * @param fxml the path to the scene resource
     * @return the controller associated with the FXML, or null if loading fails
     */
    private Object show(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );

            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof Navigable navigable) {
                navigable.setNavigator(this);
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/com/example/sudoku/css/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

            return controller;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
