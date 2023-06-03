package org.wordle.api;

import java.time.LocalDateTime;

public record Statistics(int gameId, String guessedWord, String username, LocalDateTime dateOfGame, int attemptsOnGame) {
}
