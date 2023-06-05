package org.wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wordle.api.AppUser;
import org.wordle.api.Statistics;
import org.wordle.jdbc.DataBaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/fxml/main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        List<Statistics> test = DataBaseConnector.getStatisticsByUsername("Test");
        for (Statistics statistics : test) {
            System.out.println(statistics);
        }

        System.out.println(DataBaseConnector.validateCredentials("Test2", "password"));


        launch();
    }

}