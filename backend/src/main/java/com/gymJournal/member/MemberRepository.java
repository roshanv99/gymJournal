package com.gymJournal.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Integer> {

    boolean existsMemberByEmail(String email);
    boolean existsMemberById(Integer id);
    Optional<Member> findMemberByEmail(String email);
}
