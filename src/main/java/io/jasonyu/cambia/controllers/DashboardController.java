package io.jasonyu.cambia.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import io.jasonyu.cambia.network.Lobby;
import io.jasonyu.cambia.network.User;
import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.IOException;

public class DashboardController {
    private User user;
    public boolean selfPressed = false;

    @FXML
    private JFXButton matchmakingButton;

    @FXML
    private JFXButton createLobbyButton;

    @FXML
    private JFXTextField joinCodeForm;

    @FXML
    private JFXButton joinButton;

    @FXML
    private JFXSpinner joinSpinner;

    @FXML
    private JFXSpinner matchmakingSpinner;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private Text timeElapsedLabel;

    @FXML
    private Text timeElapsed;

    @FXML
    private Text matchFoundLabel;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Text waitingForOpponent;

    @FXML
    void onCreateClick(ActionEvent event) {
        this.closeStage();
        this.loadLobby("create");
    }

    @FXML
    void onJoinLobbyClick(ActionEvent event) {

    }

    @FXML
    void onEnter(ActionEvent event) {

    }

    @FXML
    void onMatchmakingClick(ActionEvent event) {
        ((Stage) this.matchmakingButton.getScene().getWindow()).setTitle("Cambia - Finding match...");
        this.matchmakingButton.setVisible(false);
        this.createLobbyButton.setVisible(false);
        this.joinCodeForm.setVisible(false);
        this.joinButton.setVisible(false);

        this.timeElapsed.setVisible(true);
        this.timeElapsedLabel.setVisible(true);
        this.matchmakingSpinner.setVisible(true);
        this.cancelButton.setVisible(true);

        long startTime = System.currentTimeMillis();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                long elapsedSeconds = elapsedMillis / 1000;
                long elapsedMinutes = elapsedSeconds / 60;
                elapsedSeconds %= 60;
                String seconds = (elapsedSeconds < 10) ? "0" + Long.toString(elapsedSeconds) : Long.toString(elapsedSeconds);
                String minutes = (elapsedMinutes < 10) ? "0" + Long.toString(elapsedMinutes) : Long.toString(elapsedMinutes);
                timeElapsed.setText(minutes + ":" + seconds);
            }
        }.start();

        if (user.findMatch(this)) {
            this.closeStage();
            this.startMatch();
        }
        this.closeStage();
        this.startMatch();
    }

    @FXML
    void onConfirmClick(ActionEvent event) {
        this.selfPressed = true;
        this.matchmakingSpinner.setVisible(true);
        this.waitingForOpponent.setVisible(true);

        this.confirmButton.setVisible(false);
        this.matchFoundLabel.setVisible(false);
        this.cancelButton.setVisible(false);

        this.user.getSocket().emit("accept match", this.user.getCurrentLobby().getCode(), this.user.getCurrentLobby().getOtherPlayers()[0], this.user.getCurrentLobby().getOtherSocketIds()[0]);
    }

    @FXML
    void onCancelClick(ActionEvent event) {
        user.leaveQueue();
        ((Stage) this.matchmakingButton.getScene().getWindow()).setTitle("Cambia");
        this.matchmakingButton.setVisible(true);
        this.createLobbyButton.setVisible(true);
        this.joinCodeForm.setVisible(true);
        this.joinButton.setVisible(true);

        this.matchFoundLabel.setVisible(false);
        this.confirmButton.setVisible(false);
        this.timeElapsed.setVisible(false);
        this.timeElapsedLabel.setVisible(false);
        this.matchmakingSpinner.setVisible(false);
        this.cancelButton.setVisible(false);
    }

    private void findMatch() {

    }

    public void matchFound() {
        // ((Stage) matchmakingButton.getScene().getWindow()).setTitle("Cambia - Match found!");
        matchmakingSpinner.setVisible(false);
        timeElapsed.setVisible(false);
        timeElapsedLabel.setVisible(false);
        matchFoundLabel.setVisible(true);
        confirmButton.setVisible(true);
    }

    public void startMatch() {
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("game.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Cambia - Ongoing match");
            stage.setScene(new Scene(parent));
            stage.show();
            this.user.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStage() {
        ((Stage) this.matchmakingButton.getScene().getWindow()).close();
    }

    private void loadLobby(String code) {
        if (code.equalsIgnoreCase("create")) {

        }
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("lobby.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Cambia - Finding a match");
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initUser(User user) {
        this.user = user;
    }
}
