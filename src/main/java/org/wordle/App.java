package org.wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wordle.api.AppUser;
import org.wordle.api.Statistics;

import java.io.IOException;
import java.time.LocalDateTime;

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

    public static void main(String[] args) {
        // Tak się będzie używało statystyk do danego appUser
        Statistics statistics = new Statistics(1, "slowo", LocalDateTime.now(), 3);
        AppUser appUser = new AppUser("Mike", "password", statistics);
        System.out.println(appUser.statistics().dateOfGame());
        launch();
    }

}