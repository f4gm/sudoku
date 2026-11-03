package com.example.sudoku.service.navigation;

/**
 * Implemented by controllers that need to change scenes. After loading
 * an FXML file, the {@link Navigator} checks whether this interface is
 * implemented and injects itself, thereby allowing the controller to
 * navigate without needing to know about the stage.
 */
public interface Navigable {

    /**
     * Receives the navigator that created this controller.
     *
     * @param navigator the shared navigator instance
     */
    void setNavigator(Navigator navigator);
}
