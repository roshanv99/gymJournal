package com.gymJournal.member;

import com.gymJournal.exception.DuplicateResourceException;
import com.gymJournal.exception.RequestValidationException;
import com.gymJournal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private MemberService underTest;
    private final MemberDTOMapper memberDTOMapper = new MemberDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new MemberService(memberDao, memberDTOMapper, passwordEncoder);
    }

    @Test
    void getAllMembers() {
        // When
        underTest.getAllMembers();

        // Then
        verify(memberDao).selectAllMembers();
    }

    @Test
    void canGetMember() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        MemberDTO expected = memberDTOMapper.apply(member);

        // When
        MemberDTO actual = underTest.getMember(id);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetMemberReturnEmptyOptional() {
        // Given
        int id = 10;

        when(memberDao.selectMemberById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getMember(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with id [%s] not found".formatted(id));
    }

    @Test
    void addMember() {
        // Given
        String email = "alex@gmail.com";

        when(memberDao.existsMemberWithEmail(email)).thenReturn(false);

        MemberRegistrationRequest request = new MemberRegistrationRequest(
                "Alex", email, "password", 19, Gender.MALE
        );

        String passwordHash = "¢5554ml;f;lsd";

        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

        // When
        underTest.addMember(request);

        // Then
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(
                Member.class
        );

        verify(memberDao).insertMember(memberArgumentCaptor.capture());

        Member capturedMember = memberArgumentCaptor.getValue();

        assertThat(capturedMember.getId()).isNull();
        assertThat(capturedMember.getName()).isEqualTo(request.name());
        assertThat(capturedMember.getEmail()).isEqualTo(request.email());
        assertThat(capturedMember.getAge()).isEqualTo(request.age());
        assertThat(capturedMember.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingAMember() {
        // Given
        String email = "alex@gmail.com";

        when(memberDao.existsMemberWithEmail(email)).thenReturn(true);

        MemberRegistrationRequest request = new MemberRegistrationRequest(
                "Alex", email, "password", 19, Gender.MALE
        );

        // When
        assertThatThrownBy(() -> underTest.addMember(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(memberDao, never()).insertMember(any());
    }

    @Test
    void deleteMemberById() {
        // Given
        int id = 10;

        when(memberDao.existsMemberById(id)).thenReturn(true);

        // When
        underTest.deleteMemberById(id);
        // Then
        verify(memberDao).deleteMemberById(id);
    }

    @Test
    void willThrowDeleteMemberByIdNotExists() {
        // Given
        int id = 10;

        when(memberDao.existsMemberById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteMemberById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("member with id [%s] not found".formatted(id));

        // Then
        verify(memberDao, never()).deleteMemberById(id);
    }

    @Test
    void canUpdateAllMembersProperties() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        String newEmail = "alexandro@amigoscode.com";

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                "Alexandro", newEmail, 23);

        when(memberDao.existsMemberWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateMember(id, updateRequest);

        // Then
        ArgumentCaptor<Member> memberArgumentCaptor =
                ArgumentCaptor.forClass(Member.class);

        verify(memberDao).updateMember(memberArgumentCaptor.capture());
        Member capturedMember = memberArgumentCaptor.getValue();

        assertThat(capturedMember.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedMember.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedMember.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyMemberName() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                "Alexandro", null, null);

        // When
        underTest.updateMember(id, updateRequest);

        // Then
        ArgumentCaptor<Member> memberArgumentCaptor =
                ArgumentCaptor.forClass(Member.class);

        verify(memberDao).updateMember(memberArgumentCaptor.capture());
        Member capturedMember = memberArgumentCaptor.getValue();

        assertThat(capturedMember.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedMember.getAge()).isEqualTo(member.getAge());
        assertThat(capturedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void canUpdateOnlyMemberEmail() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        String newEmail = "alexandro@amigoscode.com";

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                null, newEmail, null);

        when(memberDao.existsMemberWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateMember(id, updateRequest);

        // Then
        ArgumentCaptor<Member> memberArgumentCaptor =
                ArgumentCaptor.forClass(Member.class);

        verify(memberDao).updateMember(memberArgumentCaptor.capture());
        Member capturedMember = memberArgumentCaptor.getValue();

        assertThat(capturedMember.getName()).isEqualTo(member.getName());
        assertThat(capturedMember.getAge()).isEqualTo(member.getAge());
        assertThat(capturedMember.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyMemberAge() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                null, null, 22);

        // When
        underTest.updateMember(id, updateRequest);

        // Then
        ArgumentCaptor<Member> memberArgumentCaptor =
                ArgumentCaptor.forClass(Member.class);

        verify(memberDao).updateMember(memberArgumentCaptor.capture());
        Member capturedMember = memberArgumentCaptor.getValue();

        assertThat(capturedMember.getName()).isEqualTo(member.getName());
        assertThat(capturedMember.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void willThrowWhenTryingToUpdateMemberEmailWhenAlreadyTaken() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        String newEmail = "alexandro@amigoscode.com";

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                null, newEmail, null);

        when(memberDao.existsMemberWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateMember(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(memberDao, never()).updateMember(any());
    }

    @Test
    void willThrowWhenMemberUpdateHasNoChanges() {
        // Given
        int id = 10;
        Member member = new Member(
                id, "Alex", "alex@gmail.com", "password", 19,
                Gender.MALE);
        when(memberDao.selectMemberById(id)).thenReturn(Optional.of(member));

        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                member.getName(), member.getEmail(), member.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateMember(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(memberDao, never()).updateMember(any());
    }
}