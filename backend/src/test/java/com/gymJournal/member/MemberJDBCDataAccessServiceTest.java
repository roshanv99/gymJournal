package com.gymJournal.member;

import com.gymJournal.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemberJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private MemberJDBCDataAccessService underTest;
    private final MemberRowMapper memberRowMapper = new MemberRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new MemberJDBCDataAccessService(
                getJdbcTemplate(),
                memberRowMapper
        );
    }

    @Test
    void selectAllMembers() {
        // Given
        Member member = new Member(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE);
        underTest.insertMember(member);

        // When
        List<Member> actual = underTest.selectAllMembers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectMemberById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Member> actual = underTest.selectMemberById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(member.getName());
            assertThat(c.getEmail()).isEqualTo(member.getEmail());
            assertThat(c.getAge()).isEqualTo(member.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectMemberById() {
        // Given
        int id = 0;

        // When
        var actual = underTest.selectMemberById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Member member = new Member(
                name,
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        // When
        boolean actual = underTest.existsMemberWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsMemberWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsMemberWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsMemberById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsMemberById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteMemberById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteMemberById(id);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateMemberName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When age is name
        Member update = new Member();
        update.setId(id);
        update.setName(newName);

        underTest.updateMember(update);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(member.getEmail());
            assertThat(c.getAge()).isEqualTo(member.getAge());
        });
    }

    @Test
    void updateMemberEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();;

        // When email is changed
        Member update = new Member();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateMember(update);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail); // change
            assertThat(c.getName()).isEqualTo(member.getName());
            assertThat(c.getAge()).isEqualTo(member.getAge());
        });
    }

    @Test
    void updateMemberAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When age is changed
        Member update = new Member();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateMember(update);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge); // change
            assertThat(c.getName()).isEqualTo(member.getName());
            assertThat(c.getEmail()).isEqualTo(member.getEmail());
        });
    }

    @Test
    void willUpdateAllPropertiesMember() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        // When update with new name, age and email
        Member update = new Member();
        update.setId(id);
        update.setName("foo");
        String newEmail = UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(22);

        underTest.updateMember(update);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(22);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertMember(member);

        int id = underTest.selectAllMembers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Member::getId)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        Member update = new Member();
        update.setId(id);

        underTest.updateMember(update);

        // Then
        Optional<Member> actual = underTest.selectMemberById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(member.getAge());
            assertThat(c.getName()).isEqualTo(member.getName());
            assertThat(c.getEmail()).isEqualTo(member.getEmail());
        });
    }
}