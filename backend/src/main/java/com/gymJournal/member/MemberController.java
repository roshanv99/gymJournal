package com.gymJournal.member;

import com.gymJournal.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    public MemberController(MemberService memberService,
                            JWTUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<MemberDTO> getMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("{memberId}")
    public MemberDTO getMember(
            @PathVariable("memberId") Integer memberId) {
        return memberService.getMember(memberId);
    }

    @PostMapping
    public ResponseEntity<?> registerMember(
            @RequestBody MemberRegistrationRequest request) {
        memberService.addMember(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{memberId}")
    public void deleteMember(
            @PathVariable("memberId") Integer memberId) {
        memberService.deleteMemberById(memberId);
    }

    @PutMapping("{memberId}")
    public void updateMember(
            @PathVariable("memberId") Integer memberId,
            @RequestBody MemberUpdateRequest updateRequest) {
        memberService.updateMember(memberId, updateRequest);
    }

}
