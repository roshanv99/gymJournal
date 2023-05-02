package com.gymJournal.auth;

import com.gymJournal.member.MemberDTO;

public record AuthenticationResponse (
        String token,
        MemberDTO memberDTO){
}
