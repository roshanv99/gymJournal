package com.gymJournal.member;

import java.util.List;

public record MemberDTO(
        Integer id,
        String name,
        String email,
        Gender gender,
        Integer age,
        List<String> roles,
        String username
){

}
