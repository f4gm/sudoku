package com.example.sudoku.service.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Realiza una cuenta atrás de un nivel en tiempo real utilizando un intervalo preciso de 100 ms. El temporizador
 * nunca interactúa directamente con la interfaz de usuario, comunica el progreso a través de un {@link TimerListener}
 * y se detiene automáticamente cuando la cuenta atrás llega a cero.
 */
public class Timer {
    private static final double TICK_SECONDS = 0.1;

    private final int duration;
    private double currentTime;

    private final Timeline timeline;
    private TimerListener listener;

    /**
     * Crea un temporizador para una cantidad fija de segundos.
     *
     * @param seconds duración de la cuenta atrás
     */
    public Timer(int seconds) {
        duration = seconds;
        currentTime = 0;

        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        event -> update()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void update() {
        currentTime -= TICK_SECONDS;
        if (currentTime <= 0) {
            currentTime = 0;
            stop();
            if (listener != null) {
                listener.onTimeUp();
            }
        } else if (listener != null) {
            listener.onTick(currentTime);
        }
    }

    /**
     * Registra el callback que será notificado en cada tick y en cero.
     *
     * @param listener suscriptor a notificar
     */
    public void setListener(TimerListener listener) {
        this.listener = listener;
    }

    /**
     * Inicia la cuenta atrás desde el momento actual.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Pausa la cuenta atrás, conservando el tiempo restante.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Detiene la cuenta atrás y restablece el tiempo restante a la duración
     * completa.
     */
    public void reset() {
        stop();
        currentTime = duration;
    }

    /**
     * Obtiene el tiempo actual de la cuenta atrás.
     *
     * @return segundos restantes, incluidos los decimales
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * Indica si la cuenta atrás está en curso actualmente.
     *
     * @return true mientras la cuenta atrás está avanzando
     */
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }

    /**
     * Obtiene la duración establecida para el temporizador.
     *
     * @return La duración establecida para el temporizador
     */
    public int getDuration() {
        return duration;
    }
}
