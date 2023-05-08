package com.gymJournal.category;

import com.gymJournal.member.Member;
import com.gymJournal.member.MemberRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryJDBCDataAccessService implements CategoryDao{
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRowMapper categoryRowMapper;

    public CategoryJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                       CategoryRowMapper categoryRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRowMapper = categoryRowMapper;
    }

    @Override
    public List<Category> selectAllCategories() {
        var sql = """
                SELECT *
                FROM category
                LIMIT 1000
                """;

        return jdbcTemplate.query(sql, categoryRowMapper);
    }
}
