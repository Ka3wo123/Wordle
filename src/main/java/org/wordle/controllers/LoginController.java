package org.wordle.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
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
import org.wordle.api.PropertiesSingleton;
import org.wordle.jdbc.DataBaseConnector;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Label noCredentialsLabel;
    @FXML
    private Label badCredentialsLabel;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    private Stage stage;
    private Scene scene;
    private final Timeline timelineBadCredentials = new Timeline(new KeyFrame(Duration.seconds(3), event1 -> badCredentialsLabel.setText("")));
    private final Timeline timelineNoCredentials = new Timeline(new KeyFrame(Duration.seconds(3), event1 -> noCredentialsLabel.setText("")));

    /***
     * Checks user's login and pass him to play scene if provided credentials are valid.
     * <br>
     * <b>Since now it saves username in StringProperty singleton. For every login the value is override.</b>
     */
    @FXML
    private void checkLoginAndPlay(ActionEvent event) throws IOException {
        String providedUsername = usernameLogin.getText();
        String providedPassword = passwordLogin.getText();

        if (providedPassword.isBlank() || providedUsername.isBlank()) {
            noCredentialsLabel.setText("Username and password cannot be empty!");
            timelineNoCredentials.play();
        } else {
            boolean isValid = DataBaseConnector.validateCredentials(providedUsername, providedPassword);

            if(isValid) {

                FXMLLoader loaderPlay = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/play.fxml")));

                StringProperty prop = PropertiesSingleton.getProperties();
                prop.set(providedUsername);

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(loaderPlay.load());
                scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
                stage.setScene(scene);
                stage.show();
            } else {
                badCredentialsLabel.setText("Wrong username or password");
                timelineBadCredentials.play();

            }
        }
    }

    /***
     * Returns to main menu scene.
     * @see MainMenuController
     */
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
