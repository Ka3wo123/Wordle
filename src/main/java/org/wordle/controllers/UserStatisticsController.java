package org.wordle.controllers;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.wordle.api.PropertiesSingleton;
import org.wordle.api.Statistics;
import org.wordle.jdbc.DataBaseConnector;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserStatisticsController implements Initializable {

    @FXML
    private TableColumn<Statistics, Integer> columnId;
    @FXML
    private TableColumn<Statistics, String> columnWord;
    @FXML
    private TableColumn<Statistics, LocalDateTime> columnDate;
    @FXML
    private TableColumn<Statistics, Integer> columnAttempt;

    @FXML
    private TableView<Statistics> statisticsTableView;
    private ObservableList<Statistics> statistics;
    @FXML
    private Label userStatsLabel;
    private StringProperty props;

    @FXML
    private void goPlay(ActionEvent event) throws IOException {
        FXMLLoader loadPlay = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/fxml/play.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loadPlay.load());
        scene.getStylesheets().add((Objects.requireNonNull(getClass().getResource("/styles.css"))).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialize");
        System.out.println(userStatsLabel.getText());
        props = PropertiesSingleton.getProperties();

        statistics = DataBaseConnector.getStatisticsByUsername(props.get());


        columnId.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        columnWord.setCellValueFactory(new PropertyValueFactory<>("guessedWord"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateOfGame"));
        columnAttempt.setCellValueFactory(new PropertyValueFactory<>("attemptsOnGame"));

        statisticsTableView.getColumns().setAll(columnId, columnWord, columnDate, columnAttempt);

        statisticsTableView.setItems(statistics);
    }
}
