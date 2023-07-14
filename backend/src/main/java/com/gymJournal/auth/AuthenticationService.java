package com.gymJournal.auth;

import com.gymJournal.member.Member;
import com.gymJournal.member.MemberDTO;
import com.gymJournal.member.MemberDTOMapper;
import com.gymJournal.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final MemberDTOMapper memberDTOMapper;
    private final JWTUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 MemberDTOMapper memberDTOMapper,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.memberDTOMapper = memberDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Member principal = (Member) authentication.getPrincipal();
        MemberDTO memberDTO = memberDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(memberDTO.username(), memberDTO.roles());
        return new AuthenticationResponse(token, memberDTO);
    }

}
