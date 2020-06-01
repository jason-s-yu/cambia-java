package io.jasonyu.cambia.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private JFXTextArea chatField;

    @FXML
    private JFXButton sendButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
