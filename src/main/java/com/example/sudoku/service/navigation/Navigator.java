package com.example.sudoku.service.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Alterna la ventana principal entre las tres escenas del juego. Tras
 * cargar un archivo FXML, se inyecta en los controladores {@link Navigable}
 * y proporciona datos adicionales a los controladores {@link SummaryAware}.
 */
public class Navigator {
    private final Stage stage;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    /**
     * Muestra la escena de bienvenida.
     */
    public void showWelcome() {
        show("/com/example/sudoku/view/Welcome.fxml");
    }

    /**
     * Muestra la escena del juego con un nivel 1 totalmente nuevo.
     */
    public void showGame() {
        show("/com/example/sudoku/view/Game.fxml");
    }

    /**
     * Muestra la pantalla de game over y presenta el resumen de la misma.
     *
     * @param completedLevels niveles que el jugador completó
     * @param reason motivo por el que terminó la partida
     * @param remainingSeconds segundos restantes al finalizar la partida
     */
    public void showGameOver(int completedLevels, String reason, double remainingSeconds) {
        Object controller = show("/com/example/sudoku/view/GameOver.fxml");

        if (controller instanceof SummaryAware summaryAware) {
            summaryAware.setSummary(completedLevels, reason, remainingSeconds);
        }
    }

    /**
     * Carga un archivo FXML, vincula el controlador y lo muestra en la stage.
     *
     * @param fxml ruta del recurso de la escena
     * @return el controlador asociado al FXML, o null si la carga falla
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
