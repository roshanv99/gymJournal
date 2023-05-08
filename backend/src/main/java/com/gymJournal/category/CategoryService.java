package com.gymJournal.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryDTOMapper categoryDTOMapper;
    public CategoryService(@Qualifier("jdbcCat") CategoryDao categoryDao,
                           CategoryDTOMapper categoryDTOMapper){
        this.categoryDao = categoryDao;
        this.categoryDTOMapper = categoryDTOMapper;
    }

    public List<CategoryDTO> getAllCategories(){
        return categoryDao.selectAllCategories()
                .stream()
                .map(categoryDTOMapper)
                .collect(Collectors.toList());
    }
}
