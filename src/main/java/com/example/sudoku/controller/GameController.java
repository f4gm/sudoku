package com.example.sudoku.controller;

import com.example.sudoku.model.GameState;
import com.example.sudoku.service.timer.Timer;
import com.example.sudoku.service.navigation.Navigable;
import com.example.sudoku.service.navigation.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/**
 * Controlador para la escena del juego. Conecta el modelo {@link GameState} con
 * la vista, gestiona el {@link Timer} del nivel.
 */
public class GameController implements Navigable {
    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Button helpButton;

    @FXML
    private Label statusLabel;

    @FXML
    void onHelp(){

    }





}


