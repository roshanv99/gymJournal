package com.gymJournal.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class MemberJDBCDataAccessService implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final MemberRowMapper memberRowMapper;

    public MemberJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                       MemberRowMapper memberRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberRowMapper = memberRowMapper;
    }

    @Override
    public List<Member> selectAllMembers() {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM member
                LIMIT 1000
                """;

        return jdbcTemplate.query(sql, memberRowMapper);
    }

    @Override
    public Optional<Member> selectMemberById(Integer id) {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM member
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertMember(Member member) {
        var sql = """
                INSERT INTO member(name, email, password, age, gender)
                VALUES (?, ?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getAge(),
                member.getGender().name()
        );

        System.out.println("insertMember result " + result);
    }

    @Override
    public boolean existsMemberWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM member
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsMemberById(Integer id) {
        var sql = """
                SELECT count(id)
                FROM member
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteMemberById(Integer memberId) {
        var sql = """
                DELETE
                FROM member
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, memberId);
        System.out.println("deleteMemberById result = " + result);
    }

    @Override
    public void updateMember(Member update) {
        if (update.getName() != null) {
            String sql = "UPDATE member SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
            System.out.println("update member name result = " + result);
        }
        if (update.getAge() != null) {
            String sql = "UPDATE member SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println("update member age result = " + result);
        }
        if (update.getEmail() != null) {
            String sql = "UPDATE member SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId());
            System.out.println("update member email result = " + result);
        }
    }

    @Override
    public Optional<Member> selectUserByEmail(String email) {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM member
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, email)
                .stream()
                .findFirst();
    }
}
