package com.example.sudoku.service.navigation;

/**
 * Permite al Navigator entregar el resumen de fin de partida al controlador
 * asociado a GameOver.fxml justo después de cargarlo, utilizando el mismo
 * patrón de inyección que {@link Navigable}.
 */
public interface SummaryAware {

    /**
     * Recibe el resumen de la partida cuando se muestra la escena de fin de juego.
     *
     * @param completedLevels niveles que el jugador completó
     * @param reason motivo por el que terminó la partida (p. ej., "Tiempo agotado")
     * @param remainingSeconds segundos restantes al finalizar la partida
     */
    void setSummary(int completedLevels, String reason, double remainingSeconds);
}
