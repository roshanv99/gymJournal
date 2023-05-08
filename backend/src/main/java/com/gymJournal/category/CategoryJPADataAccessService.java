package com.gymJournal.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jdbcCat")
public class CategoryJPADataAccessService implements CategoryDao{
    private final CategoryRepository categoryRepository;

    public CategoryJPADataAccessService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> selectAllCategories() {
        Page<Category> page = categoryRepository.findAll(Pageable.ofSize(1000));
        return page.getContent();
    }
}
