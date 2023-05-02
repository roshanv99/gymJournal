package com.gymJournal.member;

public record MemberUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
