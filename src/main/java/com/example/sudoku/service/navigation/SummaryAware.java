package com.example.sudoku.service.navigation;

/**
 * Allows the Navigator to pass the end-of-game summary to the controller
 * associated with GameOver.fxml immediately after loading it, using the same
 * injection pattern as {@link Navigable}.
 */
public interface SummaryAware {

    /**
     * Receives the match summary when the game-over scene is displayed.
     *
     * @param completedLevels levels completed by the player
     * @param reason the reason the match ended (e.g., "Time ran out")
     * @param remainingSeconds seconds remaining when the match ended
     */
    void setSummary(int completedLevels, String reason, double remainingSeconds);
}
