package com.gymJournal.member;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class MemberListDataAccessService implements MemberDao {

    // db
    private static final List<Member> members;

    static {
        members = new ArrayList<>();

        Member alex = new Member(
                1,
                "Alex",
                "alex@gmail.com",
                "password",
                21,
                Gender.MALE);
        members.add(alex);

        Member jamila = new Member(
                2,
                "Jamila",
                "jamila@gmail.com",
                "password",
                19,
                Gender.MALE);
        members.add(jamila);
    }

    @Override
    public List<Member> selectAllMembers() {
        return members;
    }

    @Override
    public Optional<Member> selectMemberById(Integer id) {
        return members.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertMember(Member member) {
        members.add(member);
    }

    @Override
    public boolean existsMemberWithEmail(String email) {
        return members.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsMemberById(Integer id) {
        return members.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteMemberById(Integer memberId) {
        members.stream()
                .filter(c -> c.getId().equals(memberId))
                .findFirst()
                .ifPresent(members::remove);
    }

    @Override
    public void updateMember(Member member) {
        members.add(member);
    }

    @Override
    public Optional<Member> selectUserByEmail(String email) {
        return members.stream()
                .filter(c -> c.getUsername().equals(email))
                .findFirst();
    }

}
