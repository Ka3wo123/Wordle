package org.wordle.api;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Statistics {
    private int gameId;
    private String guessedWord;
    private String username;
    private LocalDateTime dateOfGame;
    private int attemptsOnGame;

    public Statistics(int gameId, String guessedWord, String username, LocalDateTime dateOfGame, int attemptsOnGame) {
        this.gameId = gameId;
        this.guessedWord = guessedWord;
        this.username = username;
        this.dateOfGame = dateOfGame;
        this.attemptsOnGame = attemptsOnGame;
    }

    public int getGameId() {
        return gameId;
    }

    public String getGuessedWord() {
        return guessedWord;
    }

    public String getUsername() {
        return username;
    }

    public String getDateOfGame() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateOfGame.format(formatter);
    }

    public int getAttemptsOnGame() {
        return attemptsOnGame;
    }
}

