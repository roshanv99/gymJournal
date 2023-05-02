package com.gymJournal.member;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        MemberRowMapper memberRowMapper = new MemberRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Jamila");
        when(resultSet.getString("email")).thenReturn("jamila@gmail.com");
        when(resultSet.getString("gender")).thenReturn("FEMALE");

        // When
        Member actual = memberRowMapper.mapRow(resultSet, 1);

        // Then
        Member expected = new Member(
                1, "Jamila", "jamila@gmail.com", "password", 19,
                Gender.FEMALE);
        assertThat(actual).isEqualTo(expected);
    }
}