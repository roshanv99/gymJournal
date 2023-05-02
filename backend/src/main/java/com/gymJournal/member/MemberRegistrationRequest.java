package com.gymJournal.member;

public record MemberRegistrationRequest(
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {
}
