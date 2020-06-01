package io.jasonyu.cambia.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import io.jasonyu.cambia.network.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton createAccountButton;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXProgressBar loading;

    @FXML
    private Text invalidTooltip;

    @FXML
    private JFXPasswordField passwordConfirm;

    @FXML
    private JFXProgressBar strengthIndicator;

    @FXML
    private JFXTextField username;

    @FXML
    private Text passwordMatchError;

    @FXML
    private Text errorMessage;

    @FXML
    void onCreateClick(ActionEvent event) {
        if (this.passwordMatchError.isVisible()) return;

        String email = this.email.getText();
        String password = this.password.getText();
        String username = this.username.getText();
        User user = new User(email, password, username);
        String response = user.create();
        if (response.equalsIgnoreCase("success")) {
            this.closeStage();
            this.redirectToLogin();
        } else {
            this.errorMessage.setText(response);
            this.errorMessage.setVisible(true);
        }
    }

    @FXML
    void onBackClick(ActionEvent event) {
        this.closeStage();
        this.redirectToLogin();
    }

    private boolean doPasswordsMatch() {
        return this.password.getText().equals(this.passwordConfirm.getText());
    }

    private void checkPasswordChange() {
        if (!doPasswordsMatch()) {
            this.passwordMatchError.setVisible(true);
        } else {
            this.passwordMatchError.setVisible(false);
        }
        this.strengthIndicator.setProgress(scorePassword(this.password.getText()) / 80D);
    }

    private double scorePassword(String password) {
        double score = 0;

        if (password == null) return score;

        Map<Character, Double> letters = new HashMap<>();

        // award every unique letter until 5 repetitions
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (letters.get(c) != null) {
                letters.put(c, letters.get(c) + 1);
            } else {
                letters.put(c, 1D);
            }
            score += 5.0 / letters.get(password.charAt(i));
        }

        // award bonus score for variation
        Map<String, Boolean> variations = new HashMap<>();
        variations.put("digits", password.matches("/\\d/"));
        variations.put("lowercase", password.matches("/[a-z]/"));
        variations.put("uppercase", password.matches("/[A-Z]/"));
        variations.put("special", password.matches("/\\W/"));

        int varCount = 0;

        for (boolean variation : variations.values()) {
            varCount += variation ? 1 : 0;
        }

        score += (varCount - 1) * 10;

        if (score < 0) return 0D;
        return score;
    }

    private void closeStage() {
        ((Stage) this.email.getScene().getWindow()).close();
    }

    private void redirectToLogin() {
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Cambia");
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.password.textProperty().addListener((observable, oldVal, newVal) -> {
            this.checkPasswordChange();
        });
        this.passwordConfirm.textProperty().addListener((observable, oldVal, newVal) -> {
            this.checkPasswordChange();
        });
    }
}
