package com.example.sudoku.service.navigation;

/**
 * Implementada por controladores que necesitan cambiar de escena. Tras cargar
 * un archivo FXML, el {@link Navigator} comprueba si se implementa esta interfaz
 * y se inyecta a sí mismo, permitiendo así que el controlador navegue sin
 * necesidad de conocer el stage.
 */
public interface Navigable {

    /**
     * Recibe el navegador que creó este controlador.
     *
     * @param navigator la instancia compartida del navegador
     */
    void setNavigator(Navigator navigator);
}
