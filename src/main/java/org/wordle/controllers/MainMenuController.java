package org.wordle.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    private Stage stage;
    private Scene scene;

    /***
     * Switches to register controller.
     * @see RegisterController
     */
    @FXML
    public void register(ActionEvent event) throws IOException {
        FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/register.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderRegister.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /***
     * Switches to login controller.
     * @see LoginController
     */
    @FXML
    public void login(ActionEvent event) throws IOException {
        FXMLLoader loaderLogin = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderLogin.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /***
     * Switches to play controller.
     * @see PlayController
     */
    @FXML
    public void play(ActionEvent event) throws IOException {
        FXMLLoader loaderPlay = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/play.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderPlay.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    
}
