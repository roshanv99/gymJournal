package com.gymJournal.category;
import com.gymJournal.member.Gender;
import com.gymJournal.member.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("color"),
                rs.getString("description"));
    }
}
