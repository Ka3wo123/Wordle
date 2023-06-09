package org.wordle.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.wordle.api.PropertiesSingleton;
import org.wordle.jdbc.DataBaseConnector;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayController implements Initializable {

    @FXML
    private Label wordInfo;
    @FXML
    private TextFlow textFlow;
    @FXML
    private TextField wordTextField;
    @FXML
    private Label warningLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button nextWordButton;
    @FXML
    private Button statsButton;
    private int attempt;
    private String randomWord;
    private StringProperty prop = PropertiesSingleton.getProperties();
    private final Timeline timelineWarning = new Timeline(new KeyFrame(Duration.seconds(3), event1 -> warningLabel.setText("")));

    @FXML
    public boolean checkWord() {
        String providedWord = wordTextField.getText();
        if (providedWord.isBlank()) {
            wordTextField.setText("");
            warningLabel.setText("Provide a word");
            timelineWarning.play();
            return false;
        } else if (providedWord.length() != randomWord.length()) {
            wordTextField.setText("");
            warningLabel.setText("Provided word's length differs from the guessed one");
            timelineWarning.play();
            return false;
        }

        if (DataBaseConnector.checkIfWordInPoll(providedWord)) {
            if (!providedWord.equals(randomWord)) {
                for (int i = 0; i < providedWord.length(); i++) {
                    char letter = providedWord.charAt(i);
                    Text letterText = new Text(String.valueOf(letter));
                    letterText.setFont(Font.font("Arial", FontWeight.BOLD, 25));

                    for (int j = 0; j < randomWord.length(); j++) {
                        if (letter == randomWord.charAt(j) && i == j) {
                            letterText.setFill(Color.GREEN);
                            break;
                        } else if (letter == randomWord.charAt(j) && i != j && letterText.getFill() != Color.GREEN) {
                            letterText.setFill(Color.rgb(201, 149, 11));
                        } else {
                            letterText.setFill(Color.BLACK);
                        }
                    }
                    textFlow.getChildren().add(letterText);

                    if (i == providedWord.length() - 1) {
                        textFlow.getChildren().add(new Text("\n"));
                    }
                }
                wordTextField.setText("");
                if (attempt == 5) {
                    wordInfo.setText("Out of attempts! Word: " + randomWord);
                }
                attempt++;
                return false;

            }
        } else {
            wordTextField.setText("");
            warningLabel.setText("Provided word isn't in word list");
            timelineWarning.play();
            return false;
        }

        nextWordButton.setDisable(false);
        warningLabel.setTextFill(Color.GREEN);
        warningLabel.setText("CORRECT!!!");


        if(prop.get() != null) {
            DataBaseConnector.saveStatisticsForUser(usernameLabel.getText(), providedWord, attempt);
        }

        attempt = 0;
        return true;
    }

    @FXML
    private void goToStatistics(ActionEvent event) throws IOException {
        FXMLLoader loaderStat = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/user_statistics.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loaderStat.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loaderMainMenu = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/main_menu.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loaderMainMenu.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void refresh() {
        randomWord = DataBaseConnector.getRandomWord();
        wordInfo.setText("");
        wordInfo.setText(randomWord.length() + " letter word!");
        textFlow.getChildren().clear();
        wordTextField.setText("");
        nextWordButton.setDisable(true);
        warningLabel.setTextFill(Color.RED);
        warningLabel.setText("");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randomWord = DataBaseConnector.getRandomWord();
        if(prop.get() == null) {
           statsButton.setVisible(false);
        }
        usernameLabel.setText(prop.get());
        System.out.println(randomWord);
        wordInfo.setText(randomWord.length() + " letter word!");
    }

}
