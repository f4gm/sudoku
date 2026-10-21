package com.example.sudoku.service.timer;

/**
 * Notifica al código interesado sobre el progreso de la cuenta atrás.
 * El temporizador nunca sabe quién implementa esta interfaz, por lo que la capa
 * de servicio permanece libre de referencias a la interfaz de usuario.
 */
public interface TimerListener {

    /**
     * Se invoca en cada tick mientras queda tiempo.
     *
     * @param remainingSeconds segundos restantes en el nivel actual, con decimales
     */
    void onTick(double remainingSeconds);

    /**
     * Se llama una vez cuando la cuenta atrás llega a cero.
     */
    void onTimeUp();
}
