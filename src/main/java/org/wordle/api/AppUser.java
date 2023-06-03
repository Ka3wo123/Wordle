package org.wordle.api;

import java.util.UUID;

public record AppUser(UUID id, String username, String password, Statistics statistics) {
}
