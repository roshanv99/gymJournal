package com.gymJournal.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
