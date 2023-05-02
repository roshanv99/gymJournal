package com.gymJournal.member;

import com.gymJournal.exception.DuplicateResourceException;
import com.gymJournal.exception.RequestValidationException;
import com.gymJournal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final MemberDTOMapper memberDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberService(@Qualifier("jdbc") MemberDao memberDao,
                         MemberDTOMapper memberDTOMapper,
                         PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.memberDTOMapper = memberDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberDTO> getAllMembers() {
        return memberDao.selectAllMembers()
                .stream()
                .map(memberDTOMapper)
                .collect(Collectors.toList());
    }

    public MemberDTO getMember(Integer id) {
        return memberDao.selectMemberById(id)
                .map(memberDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "member with id [%s] not found".formatted(id)
                ));
    }

    public void addMember(MemberRegistrationRequest memberRegistrationRequest) {
        // check if email exists
        String email = memberRegistrationRequest.email();
        if (memberDao.existsMemberWithEmail(email)) {
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        // add
        Member member = new Member(
                memberRegistrationRequest.name(),
                memberRegistrationRequest.email(),
                passwordEncoder.encode(memberRegistrationRequest.password()),
                memberRegistrationRequest.age(),
                memberRegistrationRequest.gender());

        memberDao.insertMember(member);
    }

    public void deleteMemberById(Integer memberId) {
        if (!memberDao.existsMemberById(memberId)) {
            throw new ResourceNotFoundException(
                    "member with id [%s] not found".formatted(memberId)
            );
        }

        memberDao.deleteMemberById(memberId);
    }

    public void updateMember(Integer memberId,
                               MemberUpdateRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(memberId) as it does does not bring object into memory and instead a reference
        Member member = memberDao.selectMemberById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "member with id [%s] not found".formatted(memberId)
                ));

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(member.getName())) {
            member.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(member.getAge())) {
            member.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(member.getEmail())) {
            if (memberDao.existsMemberWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            member.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
           throw new RequestValidationException("no data changes found");
        }

        memberDao.updateMember(member);
    }
}

