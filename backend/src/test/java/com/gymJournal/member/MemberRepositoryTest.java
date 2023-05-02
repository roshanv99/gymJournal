package com.gymJournal.member;

import com.gymJournal.AbstractTestcontainers;
import com.gymJournal.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class MemberRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private MemberRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsMemberByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.MALE);

        underTest.save(member);

        // When
        var actual = underTest.existsMemberByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsMemberByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsMemberByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsMemberById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Member member = new Member(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.save(member);

        int id = underTest.findAll()
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
    void existsMemberByIdFailsWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsMemberById(id);

        // Then
        assertThat(actual).isFalse();
    }
}