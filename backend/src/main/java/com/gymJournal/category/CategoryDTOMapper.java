package com.gymJournal.category;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryDTOMapper  implements Function<Category, CategoryDTO> {
    @Override
    public CategoryDTO apply(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getTitle(),
                category.getColor(),
                category.getdescription()
        );
    }
}
