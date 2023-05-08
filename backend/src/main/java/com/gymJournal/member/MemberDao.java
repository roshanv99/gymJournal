package com.gymJournal.member;

import java.util.List;
import java.util.Optional;

interface MemberDao {
    List<Member> selectAllMembers();
    Optional<Member> selectMemberById(Integer id);
    void insertMember(Member memeber);
    boolean existsMemberWithEmail(String email);
    boolean existsMemberById(Integer id);
    void deleteMemberById(Integer memeberId);
    void updateMember(Member update);
    Optional<Member> selectUserByEmail(String email);
}
