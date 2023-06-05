package org.wordle.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    private Stage stage;
    private Scene scene;

    @FXML
    public void register(ActionEvent event) throws IOException {
        FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/register.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderRegister.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void login(ActionEvent event) throws IOException {
        FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderRegister.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void play(ActionEvent event) throws IOException {
        FXMLLoader loaderRegister = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/play.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loaderRegister.load());
        stage.setScene(scene);
        stage.show();
    }

    
}
