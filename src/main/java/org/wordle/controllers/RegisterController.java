package org.wordle.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.wordle.jdbc.DataBaseConnector;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {
    @FXML
    private Label userExistsLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private Scene scene;
    private Stage stage;
    private final Timeline timelineUserExists = new Timeline(new KeyFrame(Duration.seconds(3), event1 -> userExistsLabel.setText("")));

    @FXML
    private void checkRegistrationAndSave(ActionEvent event) throws IOException {
        String providedUsername = usernameField.getText();
        String providedPassword = passwordField.getText();

        if(providedUsername.isBlank() || providedPassword.isBlank()) {
            userExistsLabel.setText("Username and password cannot be empty");
            timelineUserExists.play();
            return;
        }

        boolean isValid = DataBaseConnector.saveNewUser(providedUsername, providedPassword);

        if(!isValid) {
            userExistsLabel.setText("Username already exists");
            timelineUserExists.play();
        } else {
            FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/main_menu.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(loaderRegister.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void goToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/main_menu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderRegister.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
