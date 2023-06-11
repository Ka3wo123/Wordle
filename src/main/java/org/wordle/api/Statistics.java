package org.wordle.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/***
 * POJO class representing app user statistics.
 * <br>
 * <b>It needs to be a class with getters instead of record due to requirements of TableView in UserStatisticsController.</b>
 * @see org.wordle.controllers.UserStatisticsController
 * @version 1.0
 */
public class Statistics {
    private final int gameId;
    private final String guessedWord;
    private final String username;
    private final LocalDateTime dateOfGame;
    private final int attemptsOnGame;

    /***
     * Parameterized constructor for statistics
     */
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

