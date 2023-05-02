package com.gymJournal.member;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberJPADataAccessServiceTest {

    private MemberJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new MemberJPADataAccessService(memberRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllMembers() {
        Page<Member> page = mock(Page.class);
        List<Member> members = List.of(new Member());
        when(page.getContent()).thenReturn(members);
        when(memberRepository.findAll(any(Pageable.class))).thenReturn(page);
        // When
        List<Member> expected = underTest.selectAllMembers();

        // Then
        assertThat(expected).isEqualTo(members);
        ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(memberRepository).findAll(pageArgumentCaptor.capture());
        assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(1000));
    }

    @Test
    void selectMemberById() {
        // Given
        int id = 1;

        // When
        underTest.selectMemberById(id);

        // Then
        verify(memberRepository).findById(id);
    }

    @Test
    void insertMember() {
        // Given
        Member member = new Member(
                1, "Ali", "ali@gmail.com", "password", 2,
                Gender.MALE);

        // When
        underTest.insertMember(member);

        // Then
        verify(memberRepository).save(member);
    }

    @Test
    void existsMemberWithEmail() {
        // Given
        String email = "foo@gmail.com";

        // When
        underTest.existsMemberWithEmail(email);

        // Then
        verify(memberRepository).existsMemberByEmail(email);
    }

    @Test
    void existsMemberById() {
        // Given
        int id = 1;

        // When
        underTest.existsMemberById(id);

        // Then
        verify(memberRepository).existsMemberById(id);
    }

    @Test
    void deleteMemberById() {
        // Given
        int id = 1;

        // When
        underTest.deleteMemberById(id);

        // Then
        verify(memberRepository).deleteById(id);
    }

    @Test
    void updateMember() {
        // Given
        Member member = new Member(
                1, "Ali", "ali@gmail.com", "password", 2,
                Gender.MALE);

        // When
        underTest.updateMember(member);

        // Then
        verify(memberRepository).save(member);
    }
}