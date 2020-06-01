package io.jasonyu.cambia.controllers;

import com.jfoenix.controls.*;
import io.jasonyu.cambia.network.Session;
import io.jasonyu.cambia.network.User;
import io.jasonyu.cambia.util.PrettyPrinter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXCheckBox remember;

    @FXML
    private Hyperlink forgot;

    @FXML
    private Hyperlink signup;

    @FXML
    private JFXProgressBar loading;

    @FXML
    private Text invalidTooltip;

    @FXML
    void onLoginClick(ActionEvent event) throws IOException, URISyntaxException {
        this.login();
    }

    @FXML
    void onEnter(ActionEvent event) throws IOException, URISyntaxException {
        this.login();
    }

    @FXML
    void onRememberToggle(ActionEvent event) {

    }

    @FXML
    void onForgotPasswordClick(ActionEvent event) {

    }

    @FXML
    void onRegisterClick(ActionEvent event) {
        this.closeStage();
        this.loadRegistration();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void login() throws IOException, URISyntaxException {
        this.login.setVisible(false);
        this.loading.setVisible(true);
        String email = this.email.getText();
        String password = this.password.getText();
        User user = new User(email, password);
        if (!user.login()) {
            this.loading.setVisible(false);
            this.login.setVisible(true);
            this.invalidTooltip.setVisible(true);
            System.out.println("Invalid credentials.");
            this.email.getStyleClass().add("wrong-credentials");
            this.password.getStyleClass().add("wrong-credentials");
        } else {
            this.loading.setVisible(false);
            this.closeStage();
            this.loadDashboard(user);
        }
    }

    private void closeStage() {
        ((Stage) this.email.getScene().getWindow()).close();
    }

    private void loadDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.initUser(user);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Cambia");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void loadRegistration() {
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Cambia - Sign up for an account");
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
