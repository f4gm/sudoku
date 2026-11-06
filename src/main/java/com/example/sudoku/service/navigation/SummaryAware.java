package com.example.sudoku.service.navigation;

/**
 * Allows the Navigator to pass the end-of-game summary to the controller
 * associated with GameOver.fxml immediately after loading it, using the same
 * injection pattern as {@link Navigable}.
 */
public interface SummaryAware {

    /**
     * Receives the message to show when the game-over scene is displayed.
     *
     * @param message text shown to the player, e.g. the victory message
     */
    void setSummary(String message);
}
