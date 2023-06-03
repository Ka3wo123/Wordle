package org.wordle.api;

import java.time.LocalDateTime;

public record Statistics(int gameId, String guessedWord, LocalDateTime dateOfGame, int attemptsOnGame) {
}
